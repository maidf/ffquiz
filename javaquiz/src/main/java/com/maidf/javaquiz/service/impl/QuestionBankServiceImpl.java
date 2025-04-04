package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.entity.po.User;
import com.maidf.javaquiz.entity.rep.BankRep;
import com.maidf.javaquiz.mapper.QuestionBankMapper;
import com.maidf.javaquiz.mapper.QuestionMapper;
import com.maidf.javaquiz.service.QuestionBankService;

@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
        implements QuestionBankService {

    @Autowired
    private QuestionBankMapper bankMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void rmById(Long id) throws Exception {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("bank_id", id);
        if (questionMapper.selectList(wrapper).isEmpty()) {
            bankMapper.deleteById(id);
        } else {
            throw new Exception("题库包含题目记录，无法删除");
        }
    }

    @Override
    public List<BankRep> listBanks() {
        var wrapper = JoinWrappers
                .lambda(QuestionBank.class)
                .selectAs(QuestionBank::getId, "id")
                .selectAs(QuestionBank::getName, "name")
                .selectAs(QuestionBank::getSubject, "subject")
                .selectAs(User::getName, "creator")
                .selectAs(QuestionBank::getCreateTime, "create_time")
                .leftJoin(User.class, User::getId, QuestionBank::getCreatorId);

        return bankMapper.selectJoinList(BankRep.class, wrapper);
    }

    @Override
    public BankRep getBankById(Long bankId) {
        var wrapper = JoinWrappers
                .lambda(QuestionBank.class)
                .selectAs(QuestionBank::getId, "id")
                .selectAs(QuestionBank::getName, "name")
                .selectAs(QuestionBank::getSubject, "subject")
                .selectAs(User::getName, "creator")
                .selectAs(QuestionBank::getCreateTime, "create_time")
                .leftJoin(User.class, User::getId, QuestionBank::getCreatorId)
                .eq(QuestionBank::getId, bankId);

        return bankMapper.selectJoinOne(BankRep.class, wrapper);
    }

}
