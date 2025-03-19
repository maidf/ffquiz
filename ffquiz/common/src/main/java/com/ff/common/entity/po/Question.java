package com.ff.common.entity.po;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.ff.common.entity.enums.DifficultyEnum;
import com.ff.common.entity.enums.QuestionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Integer questionId;
    private Integer bankId;
    private QuestionTypeEnum type;
    private String content;
    private JsonNode answer;
    private String explanation;
    private JsonNode knowledgeTags;
    private DifficultyEnum difficulty;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
