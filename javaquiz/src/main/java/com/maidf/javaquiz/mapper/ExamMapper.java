package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.rep.ExamAnsRep;
import com.maidf.javaquiz.entity.rep.ExamRep;

public interface ExamMapper extends BaseMapper<Exam> {
    @Select("""
            select
                e.id,
                p.name,
                p.time_limit,
                p.diff,
                e.score,
                p.total_score,
                e.start_time,
                e.end_time
            from
                exam e
                left join
                    paper p on p.id=e.paper_id
            where
                e.user_id=#{userId}
            """)
    List<ExamRep> selectListExam(Long userId);

    @Select("""
                select
                    a.id,
                    qb.subject as sub,
                    q.`type`,
                    q.content,
                    q.options,
                    q.answer,
                    q.ana,
                    q.diff,
                    a.user_answer
                from
                    ans_record a
                    left join
                        question q on q.id=a.question_id
                    left join
                        question_bank qb on qb.id=q.bank_id
                where
                    a.exam_id=#{examId}
            """)
    List<ExamAnsRep> selectListAns(Long examId);
}
