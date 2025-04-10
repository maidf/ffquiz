package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Component
@Data
public class EndExamReq {
    @JsonProperty("qn_id")
    private Long qnId;
    @JsonProperty("usr_ans")
    private String userAnswer;
}
