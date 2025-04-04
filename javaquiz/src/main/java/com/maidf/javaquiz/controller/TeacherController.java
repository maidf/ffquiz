package com.maidf.javaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maidf.javaquiz.entity.constant.Constant;
import com.maidf.javaquiz.entity.dto.SessionUserDto;
import com.maidf.javaquiz.entity.req.RegisterOrLoginReq;
import com.maidf.javaquiz.service.impl.UserServiceImpl;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpSession;

@CrossOrigin("*")
@RestController
@RequestMapping("teacher")
public class TeacherController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("register")
    public ResponseEntity<String> register(HttpSession session, @RequestBody RegisterOrLoginReq user) {
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
            userService.register(user.getAccount(), user.getPassword(), true);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }

        return Result.success();
    }

    @PostMapping("login")
    public ResponseEntity<String> login(HttpSession session, @RequestBody RegisterOrLoginReq user) {
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
            if (sessionUser.getIsTeacher() == false) {
                return Result.error("没有权限");
            }
            token = jwtUtil.generateToken(sessionUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }
        return Result.success(token);
    }

}
