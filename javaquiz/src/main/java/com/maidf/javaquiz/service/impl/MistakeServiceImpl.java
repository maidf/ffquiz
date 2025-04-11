package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.rep.MistakeRep;
import com.maidf.javaquiz.mapper.MistakeMapper;
import com.maidf.javaquiz.service.MistakeService;

@Service
public class MistakeServiceImpl extends ServiceImpl<MistakeMapper, Mistake>
        implements MistakeService {
    @Autowired
    private MistakeMapper mistakeMapper;

    @Override
    public List<MistakeRep> listMistake(Long userId) {
        return mistakeMapper.selectListMistake(userId);
    }

}
