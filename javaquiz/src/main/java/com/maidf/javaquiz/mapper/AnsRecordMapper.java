package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.AnsRecord;
import com.maidf.javaquiz.entity.rep.AnsRecordRep;

public interface AnsRecordMapper extends BaseMapper<AnsRecord> {
    @Select("""
                select
                    qb.subject as sub,
                    q.type,
                    q.content,
                    q.options,
                    q.answer as ans,
                    q.ana,
                    q.diff,
                    a.user_answer,
                    a.start_time,
                    a.end_time
                from
                    ans_record a
                    left join
                        user u on u.id = a.user_id
                    left join
                        question q on q.id = a.question_id
                    left join
                        question_bank qb on qb.id = q.bank_id
                where
                    u.id = #{userId}
            """)
    List<AnsRecordRep> selectListRecord(Long userId);
}
