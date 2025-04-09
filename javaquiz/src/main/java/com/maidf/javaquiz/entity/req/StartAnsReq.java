package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Component
@Data
public class StartAnsReq {
    @JsonProperty("qn_id")
    private Long questionId;
    @JsonProperty(value = "exam_id", required = false)
    private Long examId;
}
