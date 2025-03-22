package com.ff.common.entity.dto;

import org.springframework.stereotype.Component;

import com.ff.common.entity.po.QuestionBank;

import lombok.Data;

@Component
@Data
public class BankDto {
    private String name;
    private String subject; // 科目

    public QuestionBank toBank(Integer creatorId) {
        return new QuestionBank(null, name, subject, creatorId, null);
    }
}
