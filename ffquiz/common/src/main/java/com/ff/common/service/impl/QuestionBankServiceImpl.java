package com.ff.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.Question;
import com.ff.common.entity.po.QuestionBank;
import com.ff.common.mapper.QuestionBankMapper;
import com.ff.common.mapper.QuestionMapper;
import com.ff.common.service.QuestionBankService;

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

}
