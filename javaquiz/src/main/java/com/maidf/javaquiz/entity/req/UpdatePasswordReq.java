package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Component
public class UpdatePasswordReq {
    @JsonProperty("old_password")
    private String oldPassword;

    @JsonProperty("new_password")
    private String newPassword;

    private String code;
}
