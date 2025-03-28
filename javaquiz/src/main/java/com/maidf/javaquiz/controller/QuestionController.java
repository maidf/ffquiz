package com.maidf.javaquiz.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maidf.javaquiz.annotation.CheckOwnerShip;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.dto.QuestionDto;
import com.maidf.javaquiz.entity.enums.EntityTypeEnum;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.service.QuestionService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpServletRequest;

@LoginValidate(teacher = true)
@RestController
@RequestMapping("question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUtil jwtUtil;

    @LoginValidate(teacher = false)
    @GetMapping("bank/{bankId}/question")
    public ResponseEntity<String> getBankQuestion(@PathVariable Long bankId) {
        List<Question> questions = questionService.listByBankId(bankId);
        return Result.success(questions);
    }

    @LoginValidate(teacher = false)
    @GetMapping("{questionId}")
    public ResponseEntity<String> getQuestionById(@PathVariable Long questionId) {

        return Result.success(questionService.getById(questionId));
    }

    @PostMapping
    public ResponseEntity<String> createQuestion(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        Question question = mapper.readValue(entity, QuestionDto.class).toQuestion(userId);

        questionService.save(question);
        questionService.initQuestionIdsToRedis(question.getBankId());
        return Result.success();
    }

    @PostMapping("batch")
    public ResponseEntity<String> batchCreateQuestion(@RequestBody List<QuestionDto> questionDtos,
            HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        List<Question> questions = new ArrayList<>();
        questionDtos.forEach((d) -> {
            questions.add(d.toQuestion(userId));
        });

        questionService.saveBatch(questions);
        questions.forEach((q) -> {
            questionService.initQuestionIdsToRedis(q.getBankId());
        });
        return Result.success();
    }

    @CheckOwnerShip(type = EntityTypeEnum.QUESTION)
    @PutMapping("{questionId}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long questionId, @RequestBody String entity,
            HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        Question question = mapper.readValue(entity, QuestionDto.class).toQuestion(userId);
        question.setId(questionId);

        questionService.updateById(question);
        return Result.success();
    }

    @CheckOwnerShip(type = EntityTypeEnum.QUESTION)
    @DeleteMapping("{questionId}")
    public ResponseEntity<String> deleteBank(@PathVariable Long questionId) {
        Long bankId = questionService.getById(questionId).getBankId();
        questionService.rmById(questionId);
        questionService.rmQuestionIdFromRedis(bankId, questionId);
        return Result.success();
    }
}
