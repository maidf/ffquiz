package com.ff.common.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.entity.po.Question;

public interface QuestionService extends IService<Question> {
    List<Question> getByBankId(Integer bankId);

    void rmById(Integer id);
}
