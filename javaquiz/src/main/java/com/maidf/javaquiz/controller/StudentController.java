package com.maidf.javaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maidf.javaquiz.entity.constant.Constant;
import com.maidf.javaquiz.entity.dto.RegisterOrLoginDto;
import com.maidf.javaquiz.entity.dto.SessionUserDto;
import com.maidf.javaquiz.service.impl.UserServiceImpl;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpSession;

@CrossOrigin("*")
@RestController
@RequestMapping("student")
public class StudentController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("register")
    public ResponseEntity<String> register(HttpSession session, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RegisterOrLoginDto user = mapper.readValue(entity, RegisterOrLoginDto.class);
        if (user.getAccount() == null || user.getPassword() == null || user.getCaptcha() == null) {
            return Result.error("请求参数错误");
        }

        if (user.getAccount().isBlank() || user.getPassword().isBlank() || user.getCaptcha().isBlank()) {
            return Result.error("请求参数错误");
        }

        // 校验验证码
        String code = (String) session.getAttribute(Constant.CAPTCHA_KEY);
        if (!user.getCaptcha().equalsIgnoreCase(code)) {
            return Result.error("验证码错误");
        }
        try {
            userService.register(user.getAccount(), user.getPassword(), false);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }

        return Result.success();
    }

    @PostMapping("login")
    public ResponseEntity<String> login(HttpSession session, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RegisterOrLoginDto user = mapper.readValue(entity, RegisterOrLoginDto.class);
        if (user.getAccount() == null || user.getPassword() == null || user.getCaptcha() == null) {
            return Result.error("请求参数错误");
        }

        if (user.getAccount().isBlank() || user.getPassword().isBlank() || user.getCaptcha().isBlank()) {
            return Result.error("请求参数错误");
        }

        // 校验验证码
        String code = (String) session.getAttribute(Constant.CAPTCHA_KEY);
        if (!user.getCaptcha().equalsIgnoreCase(code)) {
            return Result.error("验证码错误");
        }

        String token = null;
        try {
            SessionUserDto sessionUser = userService.login(user.getAccount(), user.getPassword());
            token = jwtUtil.generateToken(sessionUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }
        return Result.success(token);
    }
}
