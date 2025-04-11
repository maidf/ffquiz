package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.rep.ExamAnsRep;
import com.maidf.javaquiz.entity.rep.ExamRep;
import com.maidf.javaquiz.entity.req.EndExamReq;
import com.maidf.javaquiz.entity.req.StartAnsReq;

public interface ExamService extends IService<Exam> {
    String startAns(StartAnsReq examMsg, Long userId) throws Exception;

    void endAns(List<EndExamReq> ansList, String token) throws Exception;

    List<ExamRep> listExam(Long userId);

    List<ExamAnsRep> listAns(Long examId);

    void rmExam(Long examId) throws Exception;
}
