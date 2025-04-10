package com.maidf.javaquiz.entity.req;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Component
@Data
public class StartAnsReq {
    @JsonProperty(value = "qn_id", required = false)
    private Long questionId;
    @JsonProperty(value = "paper_id", required = false)
    private Long paperId;
}
