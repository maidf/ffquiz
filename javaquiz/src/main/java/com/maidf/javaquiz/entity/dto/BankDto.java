package com.maidf.javaquiz.entity.dto;

import org.springframework.stereotype.Component;

import com.maidf.javaquiz.entity.po.QuestionBank;

import lombok.Data;

@Component
@Data
public class BankDto {
    private String name;
    private String subject; // 科目

    public QuestionBank toBank(Long creatorId) {
        return new QuestionBank(null, name, subject, creatorId, null);
    }
}
