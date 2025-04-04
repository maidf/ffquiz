package com.maidf.javaquiz.entity.po;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("question_bank")
public class QuestionBank implements Serializable {
    @JsonProperty(required = false)
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String subject; // 科目

    @JsonProperty(value = "creator_id", required = false)
    private Long creatorId;

    @JsonProperty(value = "create_time", required = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
