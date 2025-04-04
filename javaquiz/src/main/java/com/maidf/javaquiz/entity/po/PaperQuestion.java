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
@TableName("paper_question")
public class PaperQuestion implements Serializable {
    @JsonProperty(required = false)
    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonProperty(value = "paper_id", required = false)
    private Long paperId;

    @JsonProperty("question_id")
    private Long questionId;
    
    private Integer score;
}
