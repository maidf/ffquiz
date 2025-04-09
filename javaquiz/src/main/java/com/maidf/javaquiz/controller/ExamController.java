package com.maidf.javaquiz.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.constant.Constant;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.req.EndAnsReq;
import com.maidf.javaquiz.service.AnsRecordService;
import com.maidf.javaquiz.service.ExamService;
import com.maidf.javaquiz.service.MistakeService;
import com.maidf.javaquiz.service.PaperQuestionService;
import com.maidf.javaquiz.service.QuestionService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SecurityRequirement(name = "jwt")
@CrossOrigin("*")
@LoginValidate
@RestController
@RequestMapping("exam")
public class ExamController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnsRecordService ansRecordService;

    @Autowired
    private ExamService examService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("record")
    public ResponseEntity<String> getExamRecord(HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);
        QueryWrapper<Exam> wrapper = new QueryWrapper<Exam>();
        wrapper.eq("user_id", userId);

        return Result.success(examService.list(wrapper));
    }

    @PostMapping("end")
    public ResponseEntity<String> endExam(@RequestBody List<EndAnsReq> dtos, HttpServletRequest req,
            HttpSession session) {
        // String token = req.getHeader(jwtUtil.getHeader());
        // Long userId = jwtUtil.getLoginUserId(token);

        // Long examId = (Long) session.getAttribute(Constant.EXAM_KEY + userId);
        // Exam exam = examService.getById(examId);

        // // 保存答题记录
        // List<AnsRecord> ansRecords = new ArrayList<>();
        // dtos.forEach((d) -> {
        //     ansRecords.add(d.newExamAns(userId, examId));
        //     Question question = questionService.getById(d.getQuestionId());
        //     if (d.getUserAnswer().equals(question.getAnswer())) {
        //         Long paperId = exam.getPaperId();
        //         QueryWrapper<PaperQuestion> wrapper = new QueryWrapper<PaperQuestion>();
        //         wrapper.eq("paper_id", paperId).eq("question_id", question.getId());
        //         Integer score = paperQuestionService.getOne(wrapper).getScore();
        //         exam.setScore(exam.getScore() + score);
        //     } else {
        //         Mistake mistake = new Mistake(null, userId, question.getId(), d.getUserAnswer());
        //         mistakeService.save(mistake);
        //     }
        // });
        // ansRecordService.saveBatch(ansRecords);

        // // 更新考试记录
        // exam.setEndTime(Timestamp.from(Instant.now()));
        // examService.updateById(exam);
        // session.removeAttribute(Constant.EXAM_KEY + userId);
        return Result.success();
    }

    @PostMapping("start/{paperId}")
    public ResponseEntity<String> startExam(@PathVariable Long paperId, HttpServletRequest req) {
        // String token = req.getHeader(jwtUtil.getHeader());
        // Long userId = jwtUtil.getLoginUserId(token);

        // Timestamp now = Timestamp.from(Instant.now());
        // Exam exam = new Exam(null, userId, paperId, null, now, null);
        // examService.save(exam);

        // HttpSession session = req.getSession();
        // session.setAttribute(Constant.EXAM_KEY + userId, exam.getId());

        return Result.success();
    }

    @DeleteMapping("record/{id}")
    public ResponseEntity<String> deleteAnsRecord(@PathVariable Long id, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("id", id);
        try {
            examService.remove(wrapper);
        } catch (Exception e) {
            return Result.error("找不到记录");
        }
        return Result.success();
    }
}
