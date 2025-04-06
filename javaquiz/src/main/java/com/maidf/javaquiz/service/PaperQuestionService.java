package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.PaperQuestion;

public interface PaperQuestionService extends IService<PaperQuestion> {
    void rmQn(Long paperId, Long qnId);

    void saveBatchQn(Long paperId, List<PaperQuestion> pQs);
}
