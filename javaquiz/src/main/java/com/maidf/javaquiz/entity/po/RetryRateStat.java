package com.maidf.javaquiz.entity.po;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.enums.DifficultyEnum;

import lombok.Data;

@Data
@Component
public class RetryRateStat implements Serializable {
    private Long id;
    private String content;
    private String sub;
    @JsonProperty(value = "retry_rate")
    private Double retryRate; // 重刷率
    @JsonProperty(value = "avg_acc")
    private Double avgAcc; // 平均正确率
    private DifficultyEnum diff; // 难度
}
