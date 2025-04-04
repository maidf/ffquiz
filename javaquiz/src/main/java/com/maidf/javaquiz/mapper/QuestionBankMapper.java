package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.entity.rep.BankRep;

public interface QuestionBankMapper extends BaseMapper<QuestionBank> {
    @Select("""
                select
                    qb.id,
                    qb.name,
                    qb.subject,
                    u.name as creator,
                    qb.create_time
                from
                    question_bank qb
                    left join
                        user u on u.id = qb.creator_id
            """)
    List<BankRep> selectListBank();

    @Select("""
                select
                    qb.id,
                    qb.name,
                    qb.subject,
                    u.name as creator,
                    qb.create_time
                from
                    question_bank qb
                    left join
                        user u on u.id = qb.creator_id
                where
                    qb.id = #{bankId}
            """)
    BankRep selectBank(Long bankId);
}
