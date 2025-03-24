package com.ff.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.common.entity.po.Question;

public interface QuestionMapper extends BaseMapper<Question> {
    @Select("SELECT id FROM question WHERE bank_id = #{bankId}")
    List<Long> selectIdsByBankId(Long bankId);
}
