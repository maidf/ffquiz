package com.maidf.javaquiz.entity.po;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.enums.DifficultyEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("paper")
public class Paper implements Serializable {
    @JsonProperty(required = false)
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @JsonProperty("time_limit")
    private Integer timeLimit;

    private DifficultyEnum diff;

    @JsonProperty(value = "total_score", required = false)
    private Integer totalScore;

    @JsonProperty(value = "creator_id", required = false)
    private Long creatorId;
    
    @JsonProperty(value = "create_time", required = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
