package com.ff.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.Exam;
import com.ff.common.entity.po.Paper;
import com.ff.common.mapper.ExamMapper;
import com.ff.common.mapper.PaperMapper;
import com.ff.common.service.PaperService;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PaperMapper paperMapper;

    @Override
    public void rmById(Integer id) throws Exception {
        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", id);
        if (!examMapper.selectList(wrapper).isEmpty()) {
            throw new Exception("该试卷已投入使用，无法删除");
        }
        paperMapper.deleteById(id);
    }

}
