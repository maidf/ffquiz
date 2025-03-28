package com.maidf.javaquiz.entity.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UpdateInfoDto {
    private String name;
    private String email;
}
