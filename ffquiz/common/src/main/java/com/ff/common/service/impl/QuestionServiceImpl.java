package com.ff.common.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.constant.Constant;
import com.ff.common.entity.po.AnsRecord;
import com.ff.common.entity.po.Mistake;
import com.ff.common.entity.po.PaperQuestion;
import com.ff.common.entity.po.Question;
import com.ff.common.mapper.AnsRecordMapper;
import com.ff.common.mapper.MistakeMapper;
import com.ff.common.mapper.PaperQuestionMapper;
import com.ff.common.mapper.QuestionMapper;
import com.ff.common.service.QuestionService;

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

    @Override
    public List<Question> listByBankId(Long bankId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<Question>();
        wrapper.eq("bank_id", bankId);
        return questionMapper.selectList(wrapper);
    }

    @Override
    public void rmById(Long id) {
        Question question = questionMapper.selectById(id);
        Long bankId = question.getBankId();

        // 1. 检查是否被其他表引用
        if (isQuestionReferenced(id)) {
            bankId = rmBankId(id, bankId);
            return; // 如果被引用，不执行删除
        }

        // 2. 删除题目
        questionMapper.deleteById(id);

        // 3. 刷新 Redis 缓存（重新加载该题库的题目ID列表）
        initQuestionIdsToRedis(bankId);
    }

    // 检查题目是否被引用
    private boolean isQuestionReferenced(Long questionId) {
        // 检查 ans_record 表
        QueryWrapper<AnsRecord> ansWrapper = new QueryWrapper<>();
        ansWrapper.eq("question_id", questionId);
        if (!ansRecordMapper.exists(ansWrapper)) { // 使用 exists 优化性能
            return true;
        }

        // 检查 mistake 表
        QueryWrapper<Mistake> mistakeWrapper = new QueryWrapper<>();
        mistakeWrapper.eq("question_id", questionId);
        if (!mistakeMapper.exists(mistakeWrapper)) {
            return true;
        }

        // 检查 paper_question 表
        QueryWrapper<PaperQuestion> paperQuestionWrapper = new QueryWrapper<>();
        paperQuestionWrapper.eq("question_id", questionId);
        if (!paperQuestionMapper.exists(paperQuestionWrapper)) {
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

    @Override
    public void initQuestionIdsToRedis(Long bankId) {
        String redisKey = Constant.RANDOM_QUESTION_KEY + bankId;
        String lockKey = "lock:" + redisKey;

        // 1. 加分布式锁（防止并发重复初始化）
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(
                lockKey, (long) 1, 10, TimeUnit.SECONDS);
        if (locked == null || !locked)
            return;

        try {
            // 2. 再次检查缓存（可能其他线程已初始化）
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                return;
            }

            // 3. 从数据库查询所有题目ID
            List<Long> ids = questionMapper.selectIdsByBankId(bankId);
            if (!ids.isEmpty()) {
                redisTemplate.opsForSet().add(redisKey, ids.toArray(new Long[0]));
                redisTemplate.expire(redisKey, 1, TimeUnit.DAYS); // 设置过期时间
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

}
