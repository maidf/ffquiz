package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.entity.po.User;
import com.maidf.javaquiz.entity.rep.PaperRep;
import com.maidf.javaquiz.entity.rep.QnRep;
import com.maidf.javaquiz.mapper.ExamMapper;
import com.maidf.javaquiz.mapper.PaperMapper;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.service.PaperService;

@CacheConfig(cacheNames = "paper")
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @CacheEvict(key = "#paperId")
    @Override
    public void rmById(Long paperId) throws Exception {
        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId);
        if (!examMapper.selectList(wrapper).isEmpty()) {
            throw new Exception("该试卷已投入使用，无法删除");
        } else {
            QueryWrapper<PaperQuestion> wrapper2 = new QueryWrapper<PaperQuestion>();
            wrapper2.eq("paper_id", paperId);
            paperQuestionMapper.delete(wrapper2);
            paperMapper.deleteById(paperId);
        }
    }

    @Cacheable(key = "#paperId")
    @Override
    public List<QnRep> listQs(Long paperId) {
        var wrapper = JoinWrappers
                .lambda(PaperQuestion.class)
                .selectAs(PaperQuestion::getId, "id")
                .selectAs(QuestionBank::getName, "bank_name")
                .selectAs(Question::getType, "type")
                .selectAs(Question::getContent, "content")
                .selectAs(Question::getOptions, "options")
                .selectAs(Question::getAnswer, "answer")
                .selectAs(Question::getAna, "ana")
                .selectAs(Question::getDiff, "diff")
                .selectAs(User::getName, "creator")
                .selectAs(Question::getCreateTime, "create_time")
                .selectAs(PaperQuestion::getScore, "score")
                .leftJoin(Question.class, Question::getId, PaperQuestion::getQuestionId)
                .leftJoin(QuestionBank.class, QuestionBank::getId, Question::getBankId)
                .leftJoin(User.class, User::getId, Question::getCreatorId)
                .eq(PaperQuestion::getPaperId, paperId);

        return paperQuestionMapper.selectJoinList(QnRep.class, wrapper);
    }

    @Override
    public List<PaperRep> listPapers() {
        var wrapper = JoinWrappers
                .lambda(Paper.class)
                .selectAs(Paper::getId, "id")
                .selectAs(Paper::getName, "name")
                .selectAs(Paper::getTimeLimit, "time_limit")
                .selectAs(Paper::getDiff, "diff")
                .selectAs(Paper::getTotalScore, "total_score")
                .selectAs(User::getName, "creator")
                .selectAs(Paper::getCreateTime, "create_tiem")
                .leftJoin(User.class, User::getId, Paper::getCreatorId);

        return paperMapper.selectJoinList(PaperRep.class, wrapper);
    }

    @Override
    public PaperRep getPaperById(Long paperId) {
        var wrapper = JoinWrappers
                .lambda(Paper.class)
                .selectAs(Paper::getId, "id")
                .selectAs(Paper::getName, "name")
                .selectAs(Paper::getTimeLimit, "time_limit")
                .selectAs(Paper::getDiff, "diff")
                .selectAs(Paper::getTotalScore, "total_score")
                .selectAs(User::getName, "creator")
                .selectAs(Paper::getCreateTime, "create_tiem")
                .leftJoin(User.class, User::getId, Paper::getCreatorId)
                .eq(Paper::getId, paperId);

        return paperMapper.selectJoinOne(PaperRep.class, wrapper);
    }

}
