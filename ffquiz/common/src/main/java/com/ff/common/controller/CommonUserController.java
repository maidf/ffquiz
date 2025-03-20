package com.ff.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ff.common.entity.constant.Constant;
import com.ff.common.entity.dto.SessionUserDto;
import com.ff.common.entity.dto.UpdateInfoDto;
import com.ff.common.entity.dto.UpdatePasswordDto;
import com.ff.common.entity.po.User;
import com.ff.common.service.impl.UserServiceImpl;
import com.ff.common.util.Result;

import jakarta.servlet.http.HttpSession;

@RestController
public class CommonUserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }

    @GetMapping("info")
    public ResponseEntity<String> info(HttpSession session) {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Constant.SESSION_KEY);
        if (sessionUser == null) {
            return Result.error("未登录");
        }

        Integer userId = sessionUser.getId();
        User user = userService.getById(userId);
        return Result.success(user);
    }

    @PutMapping("info")
    public ResponseEntity<String> updateInfo(HttpSession session, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Constant.SESSION_KEY);
        if (sessionUser == null) {
            return Result.error("未登录");
        }

        // 获取用户信息
        User user = userService.getById(sessionUser.getId());

        // 读取更新信息
        ObjectMapper mapper = new ObjectMapper();
        UpdateInfoDto updateInfo = mapper.readValue(entity, UpdateInfoDto.class);

        if (updateInfo.getName() != null) {
            user.setName(updateInfo.getName());
        }
        if (updateInfo.getEmail() != null) {
            user.setEmail(updateInfo.getEmail());
        }

        userService.updateById(user);

        return Result.success();
    }

    @PutMapping("password")
    public ResponseEntity<String> updatePassword(HttpSession session, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Constant.SESSION_KEY);
        if (sessionUser == null) {
            return Result.error("未登录");
        }

        // 获取用户信息
        User user = userService.getById(sessionUser.getId());
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
    public ResponseEntity<String> logoff(HttpSession session, @PathVariable String code) {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Constant.SESSION_KEY);
        if (sessionUser == null) {
            return Result.error("未登录");
        }
        try {
            userService.logoff(sessionUser.getId(), code);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        session.invalidate();
        return Result.success();
    }
}
