package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.entity.rep.BankRep;
import com.maidf.javaquiz.mapper.QuestionBankMapper;
import com.maidf.javaquiz.mapper.QuestionMapper;
import com.maidf.javaquiz.service.QuestionBankService;

@CacheConfig(cacheNames = "qb_cache")
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
        implements QuestionBankService {

    @Autowired
    private QuestionBankMapper bankMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @CacheEvict(cacheNames = "qb_cache", allEntries = true)
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

    @Cacheable(key = "'list_banks'")
    @Override
    public List<BankRep> listBanks() {
        return bankMapper.selectListBank();
    }

    @Override
    public BankRep getBankById(Long bankId) {
        return bankMapper.selectBank(bankId);
    }

    @CacheEvict(cacheNames = "qb_cache", allEntries = true)
    @Override
    public void saveBank(QuestionBank bank) {
        super.save(bank);
    }

    @CacheEvict(cacheNames = "qb_cache", allEntries = true)
    @Override
    public void updateBank(QuestionBank bank) {
        super.updateById(bank);
    }

}
