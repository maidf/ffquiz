package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.rep.QnRep;

public interface QuestionService extends IService<Question> {

    void saveQn(Question qn);

    void updateQn(Question qn);

    List<QnRep> listQn();

    List<QnRep> listByBankId(Long bankId);

    QnRep getQnById(Long id);

    void rmById(Long id) throws Exception;

    void initQuestionIdsToRedis(Long bankId);

    void rmQuestionIdFromRedis(Long bankId, Long questionId);

    void initDailyQnIdToRedis();

    void rmQnIdFromRedis(Long qnId);

    Long getRandomQuestionId(Long bankId);

    Long getRandomQuestionId();
}
