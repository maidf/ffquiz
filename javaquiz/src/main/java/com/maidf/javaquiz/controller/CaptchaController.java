package com.maidf.javaquiz.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maidf.javaquiz.entity.constant.Constant;
import com.maidf.javaquiz.util.CaptchaUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class CaptchaController {
    @GetMapping("captcha")
    public void captcha(HttpServletResponse rep, HttpServletRequest req) throws IOException {
        // 生成验证码
        String code = CaptchaUtils.generateCode();
        BufferedImage image = CaptchaUtils.generateImage(code);

        // 存储到 Session
        HttpSession session = req.getSession();
        session.setAttribute(Constant.CAPTCHA_KEY, code);

        // 写入响应流
        CaptchaUtils.writeToResponse(image, rep);
    }
}
