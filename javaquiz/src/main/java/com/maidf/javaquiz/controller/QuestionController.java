package com.maidf.javaquiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.maidf.javaquiz.annotation.CheckOwnerShip;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.enums.EntityTypeEnum;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.rep.QnRep;
import com.maidf.javaquiz.service.QuestionService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@LoginValidate(teacher = true)
@RestController
@RequestMapping("qn")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取指定题库的所有题目
     * 
     * @param bankId
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping("bank/{bankId}/qs")
    public ResponseEntity<String> getBankQuestion(@PathVariable Long bankId) {
        List<QnRep> questions = questionService.listByBankId(bankId);
        return Result.success(questions);
    }

    /**
     * 获取指定题目
     */
    @LoginValidate(teacher = false)
    @GetMapping("{qnId}")
    public ResponseEntity<String> getQuestionById(@PathVariable Long qnId) {

        return Result.success(questionService.getQnById(qnId));
    }

    /**
     * 创建题目
     */
    @PostMapping
    public ResponseEntity<String> createQuestion(@RequestBody Question question, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        question.setCreatorId(userId);

        questionService.save(question);
        return Result.success();
    }

    /**
     * 批量创建题目
     */
    @PostMapping("batch")
    public ResponseEntity<String> batchCreateQuestion(@RequestBody List<Question> questions,
            HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        questions.forEach((e) -> {
            e.setCreatorId(userId);
        });

        questionService.saveBatch(questions);
        return Result.success();
    }

    /**
     * 更新题目
     */
    @CheckOwnerShip(type = EntityTypeEnum.QUESTION)
    @PutMapping("{qnId}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long qnId, @RequestBody Question question) {

        question.setId(qnId);

        questionService.updateById(question);
        return Result.success();
    }

    /**
     * 删除题目
     */
    @CheckOwnerShip(type = EntityTypeEnum.QUESTION)
    @DeleteMapping("{qnId}")
    public ResponseEntity<String> deleteBank(@PathVariable Long qnId) {
        Long bankId = questionService.getById(qnId).getBankId();
        questionService.rmById(qnId);
        questionService.rmQuestionIdFromRedis(bankId, qnId);
        questionService.rmQnIdFromRedis(qnId);
        return Result.success();
    }
}
