package com.maidf.javaquiz.entity.dto;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.maidf.javaquiz.entity.enums.DifficultyEnum;
import com.maidf.javaquiz.entity.enums.QuestionTypeEnum;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.po.answerType.BaseAnswer;
import com.maidf.javaquiz.entity.po.answerType.FillBlankAnswer;
import com.maidf.javaquiz.entity.po.answerType.MultipleChoiceAnswer;
import com.maidf.javaquiz.entity.po.answerType.SingleChoiceAnswer;
import com.maidf.javaquiz.entity.po.answerType.TrueFalseAnswer;

import lombok.Data;

@Component
@Data
public class QuestionDto {
    private Long bankId;
    private String type;
    private String content;
    private Map<String, String> options;
    private String answer;
    private String explanation;
    private String difficulty;

    public Question toQuestion(Long creatorId) {
        Question question = new Question(null, bankId, null, content, null, explanation, null, creatorId, null);
        BaseAnswer ans = null;
        switch (type) {
            case "fill":
                question.setType(QuestionTypeEnum.FILL_BLANK);
                ans = new FillBlankAnswer();
                ans.setCorrectAnswer(answer);
                question.setAnswer(ans);
                break;
            case "multiple":
                question.setType(QuestionTypeEnum.MULTIPLE_CHOICE);
                ans = new MultipleChoiceAnswer();
                ans.setCorrectAnswer(answer);
                ((MultipleChoiceAnswer) ans).setOptions(options);
                question.setAnswer(ans);
                break;
            case "truefalse":
                question.setType(QuestionTypeEnum.TRUE_FALSE);
                ans = new TrueFalseAnswer();
                ans.setCorrectAnswer(answer);
                question.setAnswer(ans);
                break;
            default:
                question.setType(QuestionTypeEnum.SINGLE_CHOICE);
                ans = new SingleChoiceAnswer();
                ans.setCorrectAnswer(answer);
                ((SingleChoiceAnswer) ans).setOptions(options);
                question.setAnswer(ans);
                break;
        }

        switch (difficulty) {
            case "medium":
                question.setDifficulty(DifficultyEnum.MEDIUM);
                break;
            case "hard":
                question.setDifficulty(DifficultyEnum.HARD);
                break;
            default:
                question.setDifficulty(DifficultyEnum.EASY);
                break;
        }
        return question;
    }
}
