package com.ff.common.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.po.AnsRecord;
import com.ff.common.mapper.AnsRecordMapper;
import com.ff.common.service.AnsRecordService;

@Service
public class AnsRecordServiceImpl extends ServiceImpl<AnsRecordMapper, AnsRecord>
        implements AnsRecordService {

}
