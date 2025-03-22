package com.ff.common.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.Paper;
import com.ff.common.mapper.PaperMapper;
import com.ff.common.service.PaperService;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper>
        implements PaperService {

}
