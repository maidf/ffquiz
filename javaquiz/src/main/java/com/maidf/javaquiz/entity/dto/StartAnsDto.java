package com.maidf.javaquiz.entity.dto;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.maidf.javaquiz.entity.po.AnsRecord;

import lombok.Data;

@Component
@Data
public class StartAnsDto {
    private Long questionId;
    private Long examId;

    public AnsRecord startAns(Long userId) {
        Timestamp now = Timestamp.from(Instant.now());
        AnsRecord ansRecord = new AnsRecord(null, userId, questionId, examId, null, now, null);
        return ansRecord;
    }
}
