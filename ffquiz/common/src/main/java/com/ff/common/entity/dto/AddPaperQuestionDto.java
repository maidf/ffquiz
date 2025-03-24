package com.ff.common.entity.dto;

import org.springframework.stereotype.Component;

import com.ff.common.entity.po.PaperQuestion;

import lombok.Data;

@Component
@Data
public class AddPaperQuestionDto {
    private Long paperId;
    private Long questionId;
    private Integer score;

    public PaperQuestion toPaperQuestion() {
        return new PaperQuestion(null, paperId, questionId, score);
    }
}
