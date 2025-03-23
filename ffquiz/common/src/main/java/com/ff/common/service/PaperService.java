package com.ff.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.entity.po.Paper;

public interface PaperService extends IService<Paper> {
    void rmById(Integer id) throws Exception;
}
