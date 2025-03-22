package com.ff.common.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.Exam;
import com.ff.common.mapper.ExamMapper;
import com.ff.common.service.ExamService;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam>
        implements ExamService {

}
