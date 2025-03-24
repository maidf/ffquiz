package com.ff.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ff.common.annotation.LoginValidate;
import com.ff.common.entity.po.Question;
import com.ff.common.service.QuestionService;
import com.ff.common.util.Result;

@LoginValidate
@RestController
@RequestMapping("ans")
public class AnsController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("{bankId}")
    public ResponseEntity<String> randomGetQuestion(@PathVariable Long bankId) {
        Long questionId = questionService.getRandomQuestionId(bankId);
        if (questionId == null) {
            return Result.error("没有题目");
        }

        Question question = questionService.getById(questionId);
        return Result.success(question);
    }

}
