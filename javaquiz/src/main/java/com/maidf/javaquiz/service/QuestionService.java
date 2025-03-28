package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.Question;

public interface QuestionService extends IService<Question> {
    List<Question> listByBankId(Long bankId);

    void rmById(Long id);

    void initQuestionIdsToRedis(Long bankId);

    void rmQuestionIdFromRedis(Long bankId, Long questionId);

    Long getRandomQuestionId(Long bankId);
}
