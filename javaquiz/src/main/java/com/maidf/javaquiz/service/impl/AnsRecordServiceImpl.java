package com.maidf.javaquiz.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.mapper.AnsRecordMapper;
import com.maidf.javaquiz.service.AnsRecordService;

@Service
public class AnsRecordServiceImpl extends ServiceImpl<AnsRecordMapper, AnsRecord>
        implements AnsRecordService {

}
