package com.maidf.javaquiz.entity.req;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.po.AnsRecord;

import lombok.Data;

@Component
@Data
public class EndAnsReq {
    @JsonProperty("question_id")
    private Long questionId;
    @JsonProperty("user_answer")
    private String userAnswer;

    public AnsRecord toEndAns(Long ansId) {
        Timestamp now = Timestamp.from(Instant.now());
        AnsRecord ansRecord = new AnsRecord(ansId, null, null, null, userAnswer, null, now);
        return ansRecord;
    }

    public AnsRecord newExamAns(Long userId, Long examId) {
        return new AnsRecord(null, userId, questionId, examId, userAnswer, null, null);
    }
}
