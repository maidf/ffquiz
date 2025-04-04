package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.rep.PaperRep;
import com.maidf.javaquiz.entity.rep.QnRep;

public interface PaperService extends IService<Paper> {
    void rmById(Long id) throws Exception;

    List<PaperRep> listPapers();

    PaperRep getPaperById(Long paperId);

    List<QnRep> listQs(Long paperId);
}
