package com.ff.common.entity.po.answerType;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Component
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // 使用名称作为标识
        include = JsonTypeInfo.As.PROPERTY, // 在 JSON 属性中包含类型信息
        property = "type" // 类型字段名称
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FillBlankAnswer.class, name = "FILL_BLANK"),
        @JsonSubTypes.Type(value = MultipleChoiceAnswer.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = SingleChoiceAnswer.class, name = "SINGLE_CHOICE"),
        @JsonSubTypes.Type(value = TrueFalseAnswer.class, name = "TRUE_FALSE")
})
public abstract class BaseAnswer {
    private String correctAnswer;
}
