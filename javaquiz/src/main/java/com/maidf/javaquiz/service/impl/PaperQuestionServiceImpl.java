package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.mapper.PaperMapper;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.service.PaperQuestionService;


@Service
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion>
        implements PaperQuestionService {

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Autowired
    private PaperMapper paperMapper;

    @CacheEvict(cacheNames = "paper_cache", allEntries = true)
    @Override
    public void rmQn(Long paperId, Long qnId) {
        QueryWrapper<PaperQuestion> wrapper = new QueryWrapper<PaperQuestion>();
        wrapper.eq("paper_id", paperId).eq("question_id", qnId);

        Integer minusTotalScore = 0;
        minusTotalScore += paperQuestionMapper.selectOne(wrapper).getScore();

        // 删除题目
        paperQuestionMapper.delete(wrapper);

        // 更新试卷总分
        Paper paper = paperMapper.selectById(paperId);
        paper.setTotalScore(paper.getTotalScore() - minusTotalScore);
        paperMapper.updateById(paper);
    }

    @CacheEvict(cacheNames = "paper_cache", allEntries = true)
    @Override
    public void saveBatchQn(Long paperId, List<PaperQuestion> pQs) {
        pQs.forEach(e -> {
            e.setPaperId(paperId);
        });
        // 批量插入题目
        super.saveBatch(pQs);
        // 更新试卷总分
        Integer addTotalScore = 0;
        for (PaperQuestion pQn : pQs) {
            addTotalScore += pQn.getScore();
        }
        Paper paper = paperMapper.selectById(paperId);
        paper.setTotalScore(paper.getTotalScore() + addTotalScore);
        paperMapper.updateById(paper);
    }

}
