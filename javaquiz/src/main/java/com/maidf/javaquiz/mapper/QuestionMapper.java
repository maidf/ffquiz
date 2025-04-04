package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.github.yulichang.base.MPJBaseMapper;
import com.maidf.javaquiz.entity.po.Question;

public interface QuestionMapper extends MPJBaseMapper<Question> {
    @Select("SELECT id FROM question WHERE bank_id = #{bankId}")
    List<Long> selectIdsByBankId(Long bankId);

    @Select("SELECT id FROM question WHERE bank_id IS NOT NULL")
    List<Long> selectIds();
}
