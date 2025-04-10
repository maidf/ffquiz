package com.maidf.javaquiz.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.rep.AnsRecordRep;
import com.maidf.javaquiz.entity.req.EndAnsReq;
import com.maidf.javaquiz.entity.req.StartAnsReq;
import com.maidf.javaquiz.mapper.AnsRecordMapper;
import com.maidf.javaquiz.mapper.MistakeMapper;
import com.maidf.javaquiz.mapper.QuestionMapper;
import com.maidf.javaquiz.service.AnsRecordService;
import com.maidf.javaquiz.util.AnsTokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnsRecordServiceImpl extends ServiceImpl<AnsRecordMapper, AnsRecord>
        implements AnsRecordService {

    @Autowired
    private AnsTokenUtil tokenUtil;

    @Autowired
    private AnsRecordMapper ansRecordMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private MistakeMapper mistakeMapper;

    @Override
    public String startAns(StartAnsReq ansMsg, Long userId, Long exp) throws Exception {
        try {
            String token = tokenUtil.generateToken(ansMsg, userId, exp);
            return token;
        } catch (Exception e) {
            throw new Exception("生成答题token失败");
        }
    }

    @Override
    public String startAns(StartAnsReq ansMsg, Long userId) throws Exception {
        try {
            String token = tokenUtil.generateToken(ansMsg, userId);
            return token;
        } catch (Exception e) {
            throw new Exception("生成答题token失败");
        }

    }

    @Override
    public String endAns(EndAnsReq ansMsg) throws Exception {
        String token = ansMsg.getToken();
        log.info("答题token:{}", token);
        tokenUtil.validateToken(token);

        Timestamp now = Timestamp.from(Instant.now());
        AnsRecord ansRecord = new AnsRecord(
                null,
                tokenUtil.getUserId(token),
                tokenUtil.getQnId(token),
                null,
                ansMsg.getUserAnswer(),
                tokenUtil.getStartTime(token),
                now);
        log.info("添加答题记录: {}", ansRecord);
        ansRecordMapper.insert(ansRecord);

        Question qn = questionMapper.selectById(ansRecord.getQuestionId());
        log.info("获取题目qn: {}", qn);
        if (qn == null) {
            throw new Exception("获取题目出错");
        }

        if (!qn.getAnswer().equalsIgnoreCase(ansMsg.getUserAnswer())) {
            Mistake mistake = new Mistake(null, ansRecord.getUserId(), ansRecord.getQuestionId(),
                    ansRecord.getUserAnswer());
            log.info("记录错题: {}", mistake);
            mistakeMapper.insert(mistake);
        }

        CompletableFuture.runAsync(() -> {
            try {
                log.info("拉黑token: {}", token);
                tokenUtil.blackToken(token);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        return qn.getAnswer();
    }

    @Override
    public List<AnsRecordRep> listRecord(Long userId) {
        return ansRecordMapper.selectListRecord(userId);
    }

}
