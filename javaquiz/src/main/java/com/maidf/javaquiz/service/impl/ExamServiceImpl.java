package com.maidf.javaquiz.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.rep.ExamAnsRep;
import com.maidf.javaquiz.entity.rep.ExamRep;
import com.maidf.javaquiz.entity.req.EndExamReq;
import com.maidf.javaquiz.entity.req.StartAnsReq;
import com.maidf.javaquiz.mapper.AnsRecordMapper;
import com.maidf.javaquiz.mapper.ExamMapper;
import com.maidf.javaquiz.mapper.MistakeMapper;
import com.maidf.javaquiz.mapper.PaperMapper;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.mapper.QuestionMapper;
import com.maidf.javaquiz.service.ExamService;
import com.maidf.javaquiz.util.AnsTokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam>
        implements ExamService {
    @Autowired
    private AnsTokenUtil tokenUtil;

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Autowired
    private MistakeMapper mistakeMapper;

    @Autowired
    private AnsRecordMapper ansRecordMapper;

    @Override
    public String startAns(StartAnsReq examMsg, Long userId) throws Exception {
        try {
            log.info("开始生成考试token");
            Paper paper = paperMapper.selectById(examMsg.getPaperId());
            log.info("获取paper: {}", paper);

            String token = tokenUtil.generateToken(examMsg, userId, paper.getTimeLimit().longValue() * 60 * 1000);
            log.info("生成token: {}", token);

            return token;
        } catch (Exception e) {
            throw new Exception("生成考试token失败");
        }
    }

    @Override
    public void endAns(List<EndExamReq> ansList, String token) throws Exception {
        log.info("开始保存考试记录");
        tokenUtil.validateToken(token);

        Long userId = tokenUtil.getUserId(token);
        Long paperId = tokenUtil.getPaperId(token);
        Timestamp start = tokenUtil.getStartTime(token);
        Timestamp now = Timestamp.from(Instant.now());

        Exam exam = new Exam(null, userId, paperId, 0, start, now);
        List<AnsRecord> records = new ArrayList<>();

        // 遍历提交的答案
        for (EndExamReq e : ansList) {
            Question q = questionMapper.selectById(e.getQnId());
            log.info("获取题目q: {}", q);
            if (q == null) {
                throw new Exception("获取题目出错");
            }

            // 判断答案是否正确
            if (q.getAnswer().equalsIgnoreCase(e.getUserAnswer())) {
                QueryWrapper<PaperQuestion> wrapper = new QueryWrapper<PaperQuestion>();
                wrapper.eq("paper_id", paperId).eq("question_id", q.getId());
                PaperQuestion pq = paperQuestionMapper.selectOne(wrapper);
                exam.setScore(exam.getScore() + pq.getScore());
            } else {
                Mistake mistake = new Mistake(null, userId, q.getId(),
                        e.getUserAnswer());
                log.info("记录错题: {}", mistake);
                mistakeMapper.insert(mistake);
            }
        }

        log.info("添加考试记录: {}", exam);
        examMapper.insert(exam);

        ansList.forEach(e -> {
            records.add(new AnsRecord(null, userId, e.getQnId(), exam.getId(), e.getUserAnswer(), null, null));
        });
        log.info("添加答题记录: {}", records);
        ansRecordMapper.insert(records);

        CompletableFuture.runAsync(() -> {
            try {
                log.info("拉黑token: {}", token);
                tokenUtil.blackToken(token);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

    }

    @Override
    public List<ExamRep> listExam(Long userId) {
        return examMapper.selectListExam(userId);
    }

    @Override
    public List<ExamAnsRep> listAns(Long examId) {
        return examMapper.selectListAns(examId);
    }

    @Override
    public void rmExam(Long examId) throws Exception {
        log.info("开始删除考试记录: {}", examId);

        log.info("删除题目记录");
        QueryWrapper<AnsRecord> wrapper = new QueryWrapper<AnsRecord>();
        wrapper.eq("exam_id", examId);
        try {
            ansRecordMapper.delete(wrapper);

        } catch (Exception e) {
            log.error("结束删除考试记录: {}", examId);
            throw new Exception("删除题目失败");
        }

        log.info("删除考试记录");
        try {
            examMapper.deleteById(examId);

        } catch (Exception e) {
            log.error("结束删除考试记录: {}", examId);
            throw new Exception("删除试卷失败");
        }

        log.info("结束删除考试记录: {}", examId);
    }

}
