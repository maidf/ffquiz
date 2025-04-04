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
@TableName(value = "exam")
/**
 * 考试记录
 */
public class Exam implements Serializable{
    @JsonProperty(required = false)
    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("paper_id")
    private Long paperId;

    private Integer score;

    @JsonProperty("start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    @JsonProperty("end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;
}
