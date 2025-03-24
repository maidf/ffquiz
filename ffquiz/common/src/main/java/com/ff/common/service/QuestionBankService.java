package com.ff.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.entity.po.QuestionBank;

public interface QuestionBankService extends IService<QuestionBank> {
    void rmById(Long id) throws Exception;
}
