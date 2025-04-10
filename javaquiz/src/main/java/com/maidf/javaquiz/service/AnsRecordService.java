package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.entity.rep.AnsRecordRep;
import com.maidf.javaquiz.entity.req.EndAnsReq;
import com.maidf.javaquiz.entity.req.StartAnsReq;

public interface AnsRecordService extends IService<AnsRecord> {
    String startAns(StartAnsReq ansMsg, Long userId, Long exp) throws Exception;

    String startAns(StartAnsReq ansMsg, Long userId) throws Exception;

    String endAns(EndAnsReq ansMsg) throws Exception;

    List<AnsRecordRep> listRecord(Long userId);
}
