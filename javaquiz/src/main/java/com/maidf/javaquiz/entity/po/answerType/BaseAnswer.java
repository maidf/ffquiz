package com.maidf.javaquiz.entity.po.answerType;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
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
public abstract class BaseAnswer implements Serializable{
    private String correctAnswer;
}
