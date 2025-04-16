package com.maidf.javaquiz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.constant.Constant;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.rep.QnRep;
import com.maidf.javaquiz.mapper.AnsRecordMapper;
import com.maidf.javaquiz.mapper.MistakeMapper;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.mapper.QuestionMapper;
import com.maidf.javaquiz.service.QuestionService;

import lombok.extern.slf4j.Slf4j;

@CacheConfig(cacheNames = "qn_cache")
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnsRecordMapper ansRecordMapper;

    @Autowired
    private MistakeMapper mistakeMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Cacheable(key = "'list_bank_qs' + #bankId")
    @Override
    public List<QnRep> listByBankId(Long bankId) {
        log.info("进入listByBankId:{} service", bankId);

        return questionMapper.selectBankQs(bankId);
    }

    @Cacheable(key = "'qn_id'+#qnId")
    @Override
    public QnRep getQnById(Long qnId) {
        return questionMapper.selectQn(qnId);
    }

    @CacheEvict(cacheNames = "qn_cache", allEntries = true)
    @Override
    public void rmById(Long id) {
        Question question = questionMapper.selectById(id);
        Long bankId = question.getBankId();

        // 1. 检查是否被其他表引用
        if (isQuestionReferenced(id)) {
            bankId = rmBankId(id, bankId);
            return;
        }

        // 2. 删除题目
        questionMapper.deleteById(id);
    }

    // 检查题目是否被引用
    private boolean isQuestionReferenced(Long questionId) {
        // 检查 ans_record 表
        QueryWrapper<AnsRecord> ansWrapper = new QueryWrapper<>();
        ansWrapper.eq("question_id", questionId);
        if (ansRecordMapper.exists(ansWrapper)) { // 使用 exists 优化性能
            return true;
        }

        // 检查 mistake 表
        QueryWrapper<Mistake> mistakeWrapper = new QueryWrapper<>();
        mistakeWrapper.eq("question_id", questionId);
        if (mistakeMapper.exists(mistakeWrapper)) {
            return true;
        }

        // 检查 paper_question 表
        QueryWrapper<PaperQuestion> paperQuestionWrapper = new QueryWrapper<>();
        paperQuestionWrapper.eq("question_id", questionId);
        if (paperQuestionMapper.exists(paperQuestionWrapper)) {
            return true;
        }

        return false; // 未被引用
    }

    private Long rmBankId(Long questionId, Long bankId) {
        if (bankId != null) {
            UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", questionId)
                    .set("bank_id", null);
            questionMapper.update(null, updateWrapper); // 强制更新
        }
        return bankId;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void rmQuestionIdFromRedis(Long bankId, Long questionId) {
        StringBuilder redisKeyBuilder = new StringBuilder();
        redisKeyBuilder.append(Constant.RANDOM_QUESTION_KEY);
        if (bankId != null) {
            redisKeyBuilder.append(bankId);
        }
        String redisKey = redisKeyBuilder.toString();
        String lockKey = "lock:" + redisKey;

        // 1. 加分布式锁（防止并发重复初始化）
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(
                lockKey, (long) 1, 10, TimeUnit.SECONDS);
        if (locked == null || !locked)
            return;

        try {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                redisTemplate.opsForSet().remove(redisKey, questionId);
                log.info("Redis 缓存更新成功，移除 key: " + redisKey + ", id: " + questionId);
            }
        } finally {
            redisTemplate.delete(lockKey); // 释放锁
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void initQuestionIdsToRedis(Long bankId) {
        StringBuilder redisKeyBuilder = new StringBuilder();
        redisKeyBuilder.append(Constant.RANDOM_QUESTION_KEY);
        if (bankId != null) {
            redisKeyBuilder.append(bankId);
        }
        String redisKey = redisKeyBuilder.toString();
        String lockKey = "lock:" + redisKey;

        // 1. 加分布式锁（防止并发重复初始化）
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(
                lockKey, (long) 1, 10, TimeUnit.SECONDS);
        if (locked == null || !locked)
            return;

        try {
            List<Long> ids = new ArrayList<>();
            if (bankId != null) {
                // 2. 从数据库查询所有题目ID
                ids = questionMapper.selectIdsByBankId(bankId);
            } else {
                ids = questionMapper.selectIds();
            }

            if (!ids.isEmpty()) {
                redisTemplate.opsForSet().add(redisKey, ids.toArray(new Long[0]));
                redisTemplate.expire(redisKey, 1, TimeUnit.DAYS); // 设置过期时间
                log.info("Redis 缓存更新成功，key: " + redisKey + ", ids: " + ids);
            } else {
                log.info("题库为空，未更新 Redis 缓存，key: " + redisKey);
            }
        } finally {
            redisTemplate.delete(lockKey); // 释放锁
        }
    }

    @Override
    public Long getRandomQuestionId(Long bankId) {
        String redisKey = Constant.RANDOM_QUESTION_KEY + bankId;

        // 1. 检查缓存是否存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(redisKey))) {
            initQuestionIdsToRedis(bankId); // 初始化缓存
        }

        // 2. 从Redis随机选取一个ID
        return redisTemplate.opsForSet().randomMember(redisKey);
    }

    @Override
    public Long getRandomQuestionId() {

        String dailyKey = Constant.DAILY_QUESTION_KEY;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(dailyKey))) {
            initDailyQnIdToRedis();
        }

        return redisTemplate.opsForValue().get(dailyKey);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initDailyQnIdToRedis() {
        String dailyKey = Constant.DAILY_QUESTION_KEY;
        String allQsKey = Constant.ALL_QUESTION_KEY;
        String lockKey1 = "lock:" + dailyKey;
        String lockKey2 = "lock:" + allQsKey;

        // 1. 加分布式锁（防止并发重复初始化)
        Boolean locked1 = redisTemplate.opsForValue().setIfAbsent(
                lockKey1, (long) 1, 10, TimeUnit.SECONDS);
        Boolean locked2 = redisTemplate.opsForValue().setIfAbsent(
                lockKey2, (long) 1, 10, TimeUnit.SECONDS);
        if (locked1 == null || !locked1)
            return;
        if (locked2 == null || !locked2)
            return;

        try {
            List<Long> ids = questionMapper.selectIds();
            if (!ids.isEmpty()) {
                if (Boolean.FALSE.equals(redisTemplate.hasKey(allQsKey))) {
                    redisTemplate.opsForSet().add(allQsKey, ids.toArray(new Long[0]));
                    redisTemplate.expire(allQsKey, 1, TimeUnit.DAYS);
                }
                Long qnId = redisTemplate.opsForSet().randomMember(allQsKey);
                redisTemplate.opsForValue().set(dailyKey, qnId);
            }
        } finally {
            redisTemplate.delete(lockKey1); // 释放锁
            redisTemplate.delete(lockKey2); // 释放锁
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rmQnIdFromRedis(Long qnId) {
        String dailyKey = Constant.DAILY_QUESTION_KEY;
        String allQsKey = Constant.ALL_QUESTION_KEY;
        String lockKey1 = "lock:" + dailyKey;
        String lockKey2 = "lock:" + allQsKey;

        // 1. 加分布式锁（防止并发重复初始化)
        Boolean locked1 = redisTemplate.opsForValue().setIfAbsent(
                lockKey1, (long) 1, 10, TimeUnit.SECONDS);
        Boolean locked2 = redisTemplate.opsForValue().setIfAbsent(
                lockKey2, (long) 1, 10, TimeUnit.SECONDS);
        if (locked1 == null || !locked1)
            return;
        if (locked2 == null || !locked2)
            return;

        try {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(allQsKey))) {
                redisTemplate.opsForSet().remove(allQsKey);
                log.info("Redis 缓存更新成功，移除 key: " + allQsKey);
            }
            if (Boolean.TRUE.equals(redisTemplate.hasKey(dailyKey))) {
                redisTemplate.opsForValue().getAndDelete(dailyKey);
                log.info("Redis 缓存更新成功，移除 key: " + dailyKey + ", qnId: " + qnId);
            }

        } finally {
            redisTemplate.delete(lockKey1); // 释放锁
            redisTemplate.delete(lockKey2); // 释放锁
        }
    }

    @CacheEvict(cacheNames = "qn_cache", allEntries = true)
    @Override
    public void saveQn(Question qn) {
        super.save(qn);
    }

    @CacheEvict(cacheNames = "qn_cache", allEntries = true)
    @Override
    public void updateQn(Question qn) {
        log.info("更新题目: {}", qn);
        super.updateById(qn);
    }

    @Cacheable(key = "'list_qn'")
    @Override
    public List<QnRep> listQn() {
        return questionMapper.selectListQn();
    }

}
