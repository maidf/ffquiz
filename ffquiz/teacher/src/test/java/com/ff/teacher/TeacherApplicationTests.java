package com.ff.teacher;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ff.common.entity.enums.DifficultyEnum;
import com.ff.common.entity.enums.QuestionTypeEnum;
import com.ff.common.entity.enums.UserRoleEnum;
import com.ff.common.entity.po.Question;
import com.ff.common.entity.po.QuestionBank;
import com.ff.common.entity.po.User;
import com.ff.common.entity.po.answerType.SingleChoiceAnswer;
import com.ff.common.mapper.QuestionBankMapper;
import com.ff.common.mapper.QuestionMapper;
import com.ff.common.mapper.UserMapper;

@SpringBootTest
class TeacherApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    void userTest() {
        Random random = new Random();
        User user = new User(null, "alice" + random.nextInt(), "alice123", "alice@example.com", UserRoleEnum.STUDENT,
                null);
        userMapper.insert(user);
        userMapper.selectList(null).forEach(System.out::println);

    }

    @Test
    void questionBankTest() {
        Random random = new Random();
        QuestionBank questionBank = new QuestionBank(null, "语文" + random.nextInt(), "文科", 1, null);
        questionBankMapper.insert(questionBank);
        questionBankMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    void questionTest() {
        SingleChoiceAnswer answer = new SingleChoiceAnswer();
        answer.setCorrectAnswer("A");
        Map<String, String> map = new HashMap<>();
        map.put("A", "java");
        map.put("B", "rust");
        map.put("C", "vue");
        map.put("D", "js");
        answer.setOptions(map);
        Question question = new Question(null, 1, QuestionTypeEnum.SINGLE_CHOICE, "你最喜欢的编程语言？", answer, "送分题，随便选",
                DifficultyEnum.EASY, 1, null);
        questionMapper.insert(question);
        // questionMapper.selectList(null).forEach((q) -> {
        // SingleChoiceAnswer ans = (SingleChoiceAnswer) q.getAnswer();
        // System.out.println(ans.toString());
        // });
        questionMapper.selectList(null).forEach(System.out::println);
    }

}
