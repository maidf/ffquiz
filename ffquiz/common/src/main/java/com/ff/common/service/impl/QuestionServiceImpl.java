package com.ff.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.AnsRecord;
import com.ff.common.entity.po.Mistake;
import com.ff.common.entity.po.PaperQuestion;
import com.ff.common.entity.po.Question;
import com.ff.common.mapper.AnsRecordMapper;
import com.ff.common.mapper.MistakeMapper;
import com.ff.common.mapper.PaperQuestionMapper;
import com.ff.common.mapper.QuestionMapper;
import com.ff.common.service.QuestionService;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnsRecordMapper ansRecordMapper;

    @Autowired
    private MistakeMapper mistakeMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Override
    public List<Question> getByBankId(Integer bankId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<Question>();
        wrapper.eq("bank_id", bankId);
        return questionMapper.selectList(wrapper);
    }

    @Override
    public void rmById(Integer id) {
        QueryWrapper<AnsRecord> ansWrapper = new QueryWrapper<>();
        ansWrapper.eq("question_id", id);
        if (!ansRecordMapper.selectList(ansWrapper).isEmpty()) {
            rmBankId(id);
            return;
        }

        QueryWrapper<Mistake> mistakeWrapper = new QueryWrapper<>();
        mistakeWrapper.eq("question_id", id);
        if (!mistakeMapper.selectList(mistakeWrapper).isEmpty()) {
            rmBankId(id);
            return;
        }

        QueryWrapper<PaperQuestion> paperQuestionmWrapper = new QueryWrapper<>();
        paperQuestionmWrapper.eq("question_id", id);
        if (!paperQuestionMapper.selectList(paperQuestionmWrapper).isEmpty()) {
            rmBankId(id);
            return;
        }

        questionMapper.deleteById(id);
    }

    private void rmBankId(Integer questionId) {
        Question question = questionMapper.selectById(questionId);
        if (question.getBankId() != null) {
            UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", questionId)
                    .set("bank_id", null);
            questionMapper.update(null, updateWrapper); // 强制更新
        }
    }

}
