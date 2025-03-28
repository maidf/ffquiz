package com.maidf.javaquiz.entity.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UpdatePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String code;
}
