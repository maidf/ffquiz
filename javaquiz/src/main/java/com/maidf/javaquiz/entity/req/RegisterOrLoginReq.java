package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RegisterOrLoginReq {
    private String account;
    private String password;
    private String captcha;
}
