package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Component
@Data
public class EndAnsReq {
    @JsonProperty("token")
    private String token;
    @JsonProperty("usr_ans")
    private String userAnswer;
}
