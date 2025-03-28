package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.dto.PaperQuestionDto;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.mapper.ExamMapper;
import com.maidf.javaquiz.mapper.PaperMapper;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.service.PaperService;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Override
    public void rmById(Long id) throws Exception {
        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", id);
        if (!examMapper.selectList(wrapper).isEmpty()) {
            throw new Exception("该试卷已投入使用，无法删除");
        }
        paperMapper.deleteById(id);
    }

    @Override
    public List<PaperQuestionDto> listQuestions(Long paperId) {
        return paperQuestionMapper.getQuestionsByPaperId(paperId);
    }

}
