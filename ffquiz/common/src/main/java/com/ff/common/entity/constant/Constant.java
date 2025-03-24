package com.ff.common.entity.constant;

import org.springframework.stereotype.Component;

@Component
public class Constant {
    // 验证码 Session 键名
    public static final String CAPTCHA_KEY = "captcha_key";

    // 随机题目redisKey
    public static final String RANDOM_QUESTION_KEY = "questions:bank:";
}
