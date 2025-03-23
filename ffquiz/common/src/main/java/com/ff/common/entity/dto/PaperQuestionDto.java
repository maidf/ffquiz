package com.ff.common.entity.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ff.common.entity.enums.DifficultyEnum;
import com.ff.common.entity.enums.QuestionTypeEnum;
import com.ff.common.entity.po.answerType.BaseAnswer;

import lombok.Data;

@Component
@Data
public class PaperQuestionDto {
    private Integer id;
    private String bankName;
    private QuestionTypeEnum type;
    private String content;
    private BaseAnswer answer;
    private String explanation;
    private DifficultyEnum difficulty;
    private String creatorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    private Integer score;
}
