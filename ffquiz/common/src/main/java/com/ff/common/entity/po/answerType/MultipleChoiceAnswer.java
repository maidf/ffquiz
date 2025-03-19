package com.ff.common.entity.po.answerType;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MultipleChoiceAnswer extends BaseAnswer {
    private Map<String, String> options;
}
