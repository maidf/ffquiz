package com.ff.common.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.entity.po.Question;

public interface QuestionService extends IService<Question> {
    List<Question> listByBankId(Long bankId);

    void rmById(Long id);

    void initQuestionIdsToRedis(Long bankId);

    Long getRandomQuestionId(Long bankId);
}
