package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.entity.rep.PaperQnRep;

public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {
    @Select("""
                select
                    pq.question_id as id,
                    qb.subject as sub,
                    q.type,
                    q.content,
                    q.options,
                    q.answer,
                    q.ana,
                    q.diff,
                    u.name as creator,
                    q.create_time,
                    pq.score
                from
                    paper_question pq
                    left join
                        question q on q.id = pq.question_id
                    left join
                        question_bank qb on qb.id = q.bank_id
                    left join
                        user u on u.id = q.creator_id
                where
                    pq.paper_id = #{paperId}
            """)
    List<PaperQnRep> selectListQn(Long paperId);
}
