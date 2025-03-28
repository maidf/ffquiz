package com.maidf.javaquiz.entity.dto;

import org.springframework.stereotype.Component;

import com.maidf.javaquiz.entity.po.PaperQuestion;

import lombok.Data;

@Component
@Data
public class AddPaperQuestionDto {
    private Long questionId;
    private Integer score;

    public PaperQuestion toPaperQuestion(Long paperId) {
        return new PaperQuestion(null, paperId, questionId, score);
    }
}
