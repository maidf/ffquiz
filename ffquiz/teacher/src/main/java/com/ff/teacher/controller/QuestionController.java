package com.ff.teacher.controller;

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
import com.ff.common.annotation.CheckOwnerShip;
import com.ff.common.annotation.LoginValidate;
import com.ff.common.entity.dto.QuestionDto;
import com.ff.common.entity.enums.EntityTypeEnum;
import com.ff.common.entity.po.Question;
import com.ff.common.service.QuestionService;
import com.ff.common.util.JwtUtil;
import com.ff.common.util.Result;

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
        questionService.rmById(questionId);
        return Result.success();
    }
}
