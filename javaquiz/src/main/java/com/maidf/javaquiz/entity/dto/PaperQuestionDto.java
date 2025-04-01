package com.maidf.javaquiz.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maidf.javaquiz.entity.enums.DifficultyEnum;
import com.maidf.javaquiz.entity.enums.QuestionTypeEnum;
import com.maidf.javaquiz.entity.po.answerType.BaseAnswer;

import lombok.Data;

@Component
@Data
public class PaperQuestionDto implements Serializable{
    private Long id;
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
