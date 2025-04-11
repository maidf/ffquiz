package com.maidf.javaquiz.entity.rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.enums.DifficultyEnum;
import com.maidf.javaquiz.entity.enums.QuestionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExamAnsRep {
    private Long id;

    private String sub;

    private QuestionTypeEnum type;

    private String content;

    private String options;

    private String answer;

    private String ana;

    private DifficultyEnum diff;

    @JsonProperty("usr_ans")
    private String userAnswer;
}
