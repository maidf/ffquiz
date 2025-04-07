package com.maidf.javaquiz.entity.rep;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.enums.DifficultyEnum;
import com.maidf.javaquiz.entity.enums.QuestionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaperQnRep implements Serializable{
    private Long id;
    private String sub;
    private QuestionTypeEnum type;
    private String content;
    private String options;
    private String answer;
    private String ana;
    private DifficultyEnum diff;
    private String creator;
    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    private Integer score;
}
