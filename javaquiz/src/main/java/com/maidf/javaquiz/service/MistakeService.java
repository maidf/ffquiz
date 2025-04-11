package com.maidf.javaquiz.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.rep.MistakeRep;

public interface MistakeService extends IService<Mistake> {
    List<MistakeRep> listMistake(Long userId);
}
