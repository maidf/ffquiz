package com.ff.common.entity.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SessionUserDto {
    private Long id;
    private String name;
    private Boolean isTeacher;
}
