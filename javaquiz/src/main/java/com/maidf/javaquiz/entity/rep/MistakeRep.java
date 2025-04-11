package com.maidf.javaquiz.entity.rep;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MistakeRep {
    private Long id;

    private String sub;

    private String diff;

    private String type;

    private String content;

    private String options;

    private String answer;

    @JsonProperty("usr_ans")
    private String userAnswer;

    private String ana;
}
