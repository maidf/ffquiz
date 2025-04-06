package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.entity.rep.PaperQnRep;
import com.maidf.javaquiz.entity.rep.PaperRep;
import com.maidf.javaquiz.mapper.ExamMapper;
import com.maidf.javaquiz.mapper.PaperMapper;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.service.PaperService;

@CacheConfig(cacheNames = "paper_cache")
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @CacheEvict(cacheNames = "paper_cache", allEntries = true)
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
    public List<PaperQnRep> listQs(Long paperId) {
        return paperQuestionMapper.selectListQn(paperId);
    }

    @Cacheable
    @Override
    public List<PaperRep> listPapers() {
        return paperMapper.selectListPaper();
    }

    @Override
    public PaperRep getPaperById(Long paperId) {
        return paperMapper.selectPaper(paperId);
    }

    @CacheEvict(cacheNames = "paper_cache", allEntries = true)
    @Override
    public void savePaper(Paper paper) {
        super.save(paper);
    }

    @CacheEvict(cacheNames = "paper_cache", allEntries = true)
    @Override
    public void updatePaper(Paper paper) {
        paperMapper.updateById(paper);
    }

}
