package com.ff.common.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.entity.dto.PaperQuestionDto;
import com.ff.common.entity.po.Paper;

public interface PaperService extends IService<Paper> {
    void rmById(Long id) throws Exception;

    List<PaperQuestionDto> listQuestions(Long paperId);
}
