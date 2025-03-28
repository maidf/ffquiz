package com.maidf.javaquiz.entity.dto;

import org.springframework.stereotype.Component;

import com.maidf.javaquiz.entity.enums.DifficultyEnum;
import com.maidf.javaquiz.entity.po.Paper;

import lombok.Data;

@Component
@Data
public class PaperDto {
    private String name;
    private Integer timeLimit;
    private String difficulty;

    public Paper toPaper(Long userId) {
        Paper paper = new Paper(null, name, timeLimit, null, null, userId, null);

        switch (difficulty) {
            case "medium":
                paper.setDifficulty(DifficultyEnum.MEDIUM);
                break;
            case "hard":
                paper.setDifficulty(DifficultyEnum.HARD);
                break;
            default:
                paper.setDifficulty(DifficultyEnum.EASY);
                break;
        }
        return paper;
    }
}
