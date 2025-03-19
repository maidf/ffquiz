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
        @JsonSubTypes.Type(value = FillBlankAnswer.class, name = "fillBlank"),
        @JsonSubTypes.Type(value = MultipleChoiceAnswer.class, name = "multipleChoice"),
        @JsonSubTypes.Type(value = SingleChoiceAnswer.class, name = "singleChoice"),
        @JsonSubTypes.Type(value = TrueFalseAnswer.class, name = "trueFalse")
})
public abstract class BaseAnswer {
    private String correctAnswer;
}
