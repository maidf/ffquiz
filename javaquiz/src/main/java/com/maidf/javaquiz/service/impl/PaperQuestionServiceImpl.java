package com.maidf.javaquiz.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.mapper.PaperQuestionMapper;
import com.maidf.javaquiz.service.PaperQuestionService;

@Service
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion>
        implements PaperQuestionService {

}
