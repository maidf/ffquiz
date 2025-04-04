package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UpdateInfoReq {
    private String name;
    private String email;
}
