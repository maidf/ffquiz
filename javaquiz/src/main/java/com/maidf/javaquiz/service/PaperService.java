package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.rep.PaperQnRep;
import com.maidf.javaquiz.entity.rep.PaperRep;

public interface PaperService extends IService<Paper> {
    void rmById(Long id) throws Exception;

    List<PaperRep> listPapers();

    PaperRep getPaperById(Long paperId);

    List<PaperQnRep> listQs(Long paperId);

    void savePaper(Paper paper);

    void updatePaper(Paper paper);
}
