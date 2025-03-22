package com.ff.common.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.Mistake;
import com.ff.common.mapper.MistakeMapper;
import com.ff.common.service.MistakeService;

@Service
public class MistakeServiceImpl extends ServiceImpl<MistakeMapper, Mistake>
        implements MistakeService {

}
