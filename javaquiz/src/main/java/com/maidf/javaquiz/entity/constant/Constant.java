package com.maidf.javaquiz.entity.constant;

import org.springframework.stereotype.Component;

@Component
public class Constant {
    // 验证码 Session 键名
    public static final String CAPTCHA_KEY = "captcha_key";

    // 随机题目 redisKey
    public static final String RANDOM_QUESTION_KEY = "questions:bank:";

    // 所有题目 redisKey
    public static final String ALL_QUESTION_KEY = "all_qs";

    // 每日题目 redisKey
    public static final String DAILY_QUESTION_KEY = "daily_qn";

    // 考试 Session 键名
    public static final String EXAM_KEY = "exam_key";

    // 刷题 Session 键名
    public static final String ANS_KEY = "ans_key";
}
