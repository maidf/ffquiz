package com.maidf.javaquiz.entity.po;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maidf.javaquiz.entity.enums.DifficultyEnum;
import com.maidf.javaquiz.entity.enums.QuestionTypeEnum;
import com.maidf.javaquiz.entity.po.answerType.BaseAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "question", autoResultMap = true)
public class Question implements Serializable{
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long bankId;
    private QuestionTypeEnum type;
    private String content;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private BaseAnswer answer;
    private String explanation;
    private DifficultyEnum difficulty;
    private Long creatorId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
