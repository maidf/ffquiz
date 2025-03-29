package com.maidf.javaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.dto.UpdateInfoDto;
import com.maidf.javaquiz.entity.dto.UpdatePasswordDto;
import com.maidf.javaquiz.entity.po.User;
import com.maidf.javaquiz.service.impl.UserServiceImpl;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LoginValidate
@RestController
@RequestMapping("usr")
public class CommonUserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest req, HttpSession session) {
        String token = req.getHeader(jwtUtil.getHeader());
        try {
            userService.logout(token);
        } catch (Exception e) {
            return Result.error("验证身份失败");
        }
        ;
        session.invalidate();
        return Result.success();
    }

    @GetMapping("info")
    public ResponseEntity<String> info(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);
        User user = userService.getById(userId);
        return Result.success(user);
    }

    @PutMapping("info")
    public ResponseEntity<String> updateInfo(HttpServletRequest req, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        // 获取用户信息
        User user = userService.getById(userId);

        // 读取更新信息
        ObjectMapper mapper = new ObjectMapper();
        UpdateInfoDto updateInfo = mapper.readValue(entity, UpdateInfoDto.class);

        if (updateInfo.getName() != null && !updateInfo.getName().isBlank()) {
            user.setName(updateInfo.getName());
        }
        if (updateInfo.getEmail() != null && !updateInfo.getEmail().isBlank()) {
            user.setEmail(updateInfo.getEmail());
        }

        userService.updateById(user);

        return Result.success();
    }

    @PutMapping("password")
    public ResponseEntity<String> updatePassword(HttpServletRequest req, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        // 获取用户信息
        User user = userService.getById(userId);
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            return Result.error("未绑定邮箱");
        }

        // 读取更新信息
        ObjectMapper mapper = new ObjectMapper();
        UpdatePasswordDto updatePasswordDto = mapper.readValue(entity, UpdatePasswordDto.class);

        if (updatePasswordDto.getNewPassword() == null || updatePasswordDto.getNewPassword().isBlank()) {
            return Result.error("修改失败");
        }

        try {
            userService.updatePassword(updatePasswordDto, user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        return Result.success();
    }

    @DeleteMapping("logoff/{code}")
    public ResponseEntity<String> logoff(HttpServletRequest req, @PathVariable String code) {
        HttpSession session = req.getSession();
        String token = req.getHeader(jwtUtil.getHeader());
        if (token == null) {
            return Result.error("未登录");
        }
        try {
            userService.logoff(token, code);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        session.invalidate();
        return Result.success();
    }
}
