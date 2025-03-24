package com.ff.common.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.PaperQuestion;
import com.ff.common.mapper.PaperQuestionMapper;
import com.ff.common.service.PaperQuestionService;

@Service
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion>
        implements PaperQuestionService {

}
