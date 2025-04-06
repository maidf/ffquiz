package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.rep.QnRep;

public interface QuestionMapper extends BaseMapper<Question> {
    @Select("SELECT id FROM question WHERE bank_id = #{bankId}")
    List<Long> selectIdsByBankId(Long bankId);

    @Select("SELECT id FROM question WHERE bank_id IS NOT NULL")
    List<Long> selectIds();

    @Select("""
                select
                    q.id,
                    qb.subject as sub,
                    q.type,
                    q.content,
                    q.options,
                    q.answer,
                    q.ana,
                    q.diff,
                    u.name as creator,
                    q.create_time
                from
                    question q
                    left join
                        user u on u.id = q.creator_id
                    left join
                        question_bank qb on qb.id = q.bank_id
                where
                    q.bank_id = #{bankId}
            """)
    List<QnRep> selectListQn(Long bankId);

    @Select("""
                select
                    q.id,
                    qb.subject as sub,
                    q.type,
                    q.content,
                    q.options,
                    q.answer,
                    q.ana,
                    q.diff,
                    u.name as creator,
                    q.create_time
                from
                    question q
                    left join
                        user u on u.id = q.creator_id
                    left join
                        question_bank qb on qb.id = q.bank_id
                where
                    q.bank_id is not null
            """)
    List<QnRep> selectListQn();

    @Select("""
                select
                    q.id,
                    qb.subject as sub,
                    q.type,
                    q.content,
                    q.options,
                    q.answer,
                    q.ana,
                    q.diff,
                    u.name as creator,
                    q.create_time
                from
                    question q
                    left join
                        user u on u.id = q.creator_id
                    left join
                        question_bank qb on qb.id = q.bank_id
                where
                    q.id = #{qnId}
            """)
    QnRep selectQn(Long qnId);
}
