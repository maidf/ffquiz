package com.maidf.javaquiz.entity.req;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.po.AnsRecord;

import lombok.Data;

@Component
@Data
public class StartAnsReq {
    @JsonProperty("question_id")
    private Long questionId;
    @JsonProperty("exam_id")
    private Long examId;

    public AnsRecord startAns(Long userId) {
        Timestamp now = Timestamp.from(Instant.now());
        AnsRecord ansRecord = new AnsRecord(null, userId, questionId, examId, null, now, null);
        return ansRecord;
    }
}
