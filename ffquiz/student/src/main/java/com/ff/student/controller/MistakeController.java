package com.ff.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ff.common.annotation.LoginValidate;
import com.ff.common.entity.po.Mistake;
import com.ff.common.service.MistakeService;
import com.ff.common.util.JwtUtil;
import com.ff.common.util.Result;

import jakarta.servlet.http.HttpServletRequest;

@LoginValidate
@RestController
@RequestMapping("mistake")
public class MistakeController {
    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("record")
    public ResponseEntity<String> getAnsRecord(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);
        QueryWrapper<Mistake> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        return Result.success(mistakeService.list(wrapper));
    }

}
