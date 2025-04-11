package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.Mistake;
import com.maidf.javaquiz.entity.rep.MistakeRep;

public interface MistakeMapper extends BaseMapper<Mistake> {
    @Select("""
                select
                    m.id,
                    qb.subject as sub,
                    q.diff,
                    q.`type`,
                    q.content,
                    q.options,
                    q.answer,
                    m.user_answer,
                    q.ana
                from
                    mistake m
                    left join
                        question q on q.id=m.question_id
                    left join
                        question_bank qb on qb.id=q.bank_id
                where
                    m.user_id=#{userId}
            """)
    List<MistakeRep> selectListMistake(Long userId);
}
