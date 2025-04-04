package com.maidf.javaquiz.entity.po;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("mistake")
public class Mistake implements Serializable{
    @JsonProperty(required = false)
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("question_id")
    private Long questionId;
    @JsonProperty("user_answer")
    private String userAnswer;
}
