package com.maidf.javaquiz.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.mapper.ExamMapper;
import com.maidf.javaquiz.service.ExamService;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam>
        implements ExamService {

}
