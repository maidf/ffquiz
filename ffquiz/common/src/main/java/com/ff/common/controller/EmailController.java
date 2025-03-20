package com.ff.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ff.common.entity.constant.Constant;
import com.ff.common.entity.dto.SessionUserDto;
import com.ff.common.entity.enums.EmailMsgEnum;
import com.ff.common.entity.po.User;
import com.ff.common.service.UserService;
import com.ff.common.util.Result;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("email")
public class EmailController {
    @Autowired
    private UserService userService;

    @GetMapping("resetPassword")
    public ResponseEntity<String> resetPassword(HttpSession session) {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Constant.SESSION_KEY);
        if (sessionUser == null) {
            return Result.error("未登录");
        }
        User user = userService.getById(sessionUser.getId());
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
    public ResponseEntity<String> logoff(HttpSession session) {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Constant.SESSION_KEY);
        if (sessionUser == null) {
            return Result.error("未登录");
        }
        User user = userService.getById(sessionUser.getId());
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
