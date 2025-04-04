package com.maidf.javaquiz.controller;

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
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.req.EndAnsReq;
import com.maidf.javaquiz.entity.req.StartAnsReq;
import com.maidf.javaquiz.service.AnsRecordService;
import com.maidf.javaquiz.service.MistakeService;
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
@RequestMapping("ans")
public class AnsController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnsRecordService ansRecordService;

    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 从指定题库中随机抽题
     */
    @GetMapping("{bankId}")
    public ResponseEntity<String> randomGetQuestion(@PathVariable Long bankId) {
        Long questionId = questionService.getRandomQuestionId(bankId);
        if (questionId == null) {
            return Result.error("没有题目");
        }

        Question question = questionService.getById(questionId);
        return Result.success(question);
    }

    /**
     * 从所有题目中随机抽题
     */
    @GetMapping("daily")
    public ResponseEntity<String> dailyQuestion() {
        Long questionId = questionService.getRandomQuestionId();
        if (questionId == null) {
            return Result.error("没有题目");
        }

        Question question = questionService.getById(questionId);
        return Result.success(question);
    }

    @GetMapping("record")
    public ResponseEntity<String> getAnsRecord(HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);
        QueryWrapper<AnsRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        return Result.success(ansRecordService.list(wrapper));
    }

    @PostMapping("end")
    public ResponseEntity<String> ansQuestion(@RequestBody EndAnsReq endAnsDto, HttpSession session,
            HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        Long ansId = (Long) session.getAttribute(Constant.RANDOM_QUESTION_KEY + userId);
        AnsRecord ansRecord = endAnsDto.toEndAns(ansId);
        ansRecordService.updateById(ansRecord);
        session.removeAttribute(Constant.EXAM_KEY + userId);

        // 记录错题
        Question question = questionService.getById(ansRecord.getQuestionId());
        if (!ansRecord.getUserAnswer().equals(question.getAnswer())) {
            Mistake mistake = new Mistake(null, userId, question.getId(), ansRecord.getUserAnswer());
            mistakeService.save(mistake);
        }
        return Result.success();
    }

    @PostMapping("start")
    public ResponseEntity<String> startAnswer(@RequestBody StartAnsReq startAnsDto, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        AnsRecord ansRecord = startAnsDto.startAns(userId);
        ansRecordService.save(ansRecord);

        HttpSession session = req.getSession();
        session.setAttribute(Constant.RANDOM_QUESTION_KEY + userId, ansRecord.getId());

        return Result.success();
    }

    @DeleteMapping("record/{id}")
    public ResponseEntity<String> deleteAnsRecord(@PathVariable Long id, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        QueryWrapper<AnsRecord> wrapper = new QueryWrapper<AnsRecord>();
        wrapper.eq("user_id", userId).eq("id", id);
        try {
            ansRecordService.remove(wrapper);
        } catch (Exception e) {
            return Result.error("找不到记录");
        }
        return Result.success();
    }

}
