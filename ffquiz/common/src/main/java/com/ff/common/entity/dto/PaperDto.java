package com.ff.common.entity.dto;

import org.springframework.stereotype.Component;

import com.ff.common.entity.enums.DifficultyEnum;
import com.ff.common.entity.po.Paper;

import lombok.Data;

@Component
@Data
public class PaperDto {
    private String name;
    private Integer timeLimit;
    private String difficulty;

    public Paper toPaper(Integer userId) {
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
