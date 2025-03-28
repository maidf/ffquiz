package com.maidf.javaquiz.entity.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RegisterOrLoginDto {
    private String account;
    private String password;
    private String captcha;
}
