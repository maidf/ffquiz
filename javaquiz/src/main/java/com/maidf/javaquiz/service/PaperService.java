package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.dto.PaperQuestionDto;
import com.maidf.javaquiz.entity.po.Paper;

public interface PaperService extends IService<Paper> {
    void rmById(Long id) throws Exception;

    List<PaperQuestionDto> listQuestions(Long paperId);
}
