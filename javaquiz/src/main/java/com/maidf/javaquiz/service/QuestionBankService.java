package com.maidf.javaquiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.QuestionBank;

public interface QuestionBankService extends IService<QuestionBank> {
    void rmById(Long id) throws Exception;
}
