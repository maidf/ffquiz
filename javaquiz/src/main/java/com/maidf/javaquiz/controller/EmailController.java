package com.maidf.javaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.enums.EmailMsgEnum;
import com.maidf.javaquiz.entity.po.User;
import com.maidf.javaquiz.service.UserService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpServletRequest;

@LoginValidate
@RestController
@RequestMapping("email")
public class EmailController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("resetPassword")
    public ResponseEntity<String> resetPassword(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);
        User user = userService.getById(userId);
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            return Result.error("未绑定邮箱");
        }
        try {
            userService.sendEmail(user.getId().toString(), email,
                    EmailMsgEnum.RESET_PASSWORD);
        } catch (Exception e) {
            return Result.error("未绑定邮箱");
        }
        return Result.success();
    }

    @GetMapping("logoff")
    public ResponseEntity<String> logoff(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        User user = userService.getById(userId);
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            return Result.error("未绑定邮箱");
        }
        try {
            userService.sendEmail(user.getId().toString(), email,
                    EmailMsgEnum.LOGOFF);
        } catch (Exception e) {
            return Result.error("未绑定邮箱");
        }
        return Result.success();
    }
}
