package com.maidf.javaquiz.entity.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maidf.javaquiz.entity.po.QuestionBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
public class BankRes {
    private Long id;
    private String name;
    private String subject;
    private String creator;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    public BankRes(QuestionBank bank, String creatorName) {
        this.id = bank.getId();
        this.name = bank.getName();
        this.subject = bank.getSubject();
        this.creator = creatorName;
        this.createTime = bank.getCreateTime();
    }
}
