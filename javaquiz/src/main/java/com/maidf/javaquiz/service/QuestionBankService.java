package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.entity.rep.BankRep;

public interface QuestionBankService extends IService<QuestionBank> {
    void rmById(Long id) throws Exception;

    List<BankRep> listBanks();

    BankRep getBankById(Long bankId);
}
