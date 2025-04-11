package com.maidf.javaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.service.MistakeService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@SecurityRequirement(name = "jwt")
@CrossOrigin("*")
@LoginValidate
@RestController
@RequestMapping("mistake")
public class MistakeController {
    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<String> getAnsRecord(HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        return Result.success(mistakeService.listMistake(userId));
    }

}
