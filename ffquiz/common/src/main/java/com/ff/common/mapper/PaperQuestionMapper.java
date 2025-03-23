package com.ff.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.common.entity.dto.PaperQuestionDto;
import com.ff.common.entity.po.PaperQuestion;

public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {
    @Select("SELECT\r\n" + //
            "    pq.id AS id,\r\n" + //
            "    qb.name AS bank_name,\r\n" + //
            "    q.type AS type,\r\n" + //
            "    q.content AS content,\r\n" + //
            "    q.answer AS answer,\r\n" + //
            "    q.explanation AS explanation,\r\n" + //
            "    q.difficulty AS difficulty,\r\n" + //
            "    u.name AS creator_name,\r\n" + //
            "    q.create_time AS create_time,\r\n" + //
            "    pq.score AS score\r\n" + //
            "FROM\r\n" + //
            "    paper_question pq\r\n" + //
            "JOIN\r\n" + //
            "    question q ON pq.question_id = q.id\r\n" + //
            "JOIN\r\n" + //
            "    question_bank qb ON q.bank_id = qb.id\r\n" + //
            "JOIN\r\n" + //
            "    user u ON q.creator_id = u.id\r\n" + //
            "WHERE\r\n" + //
            "    pq.paper_id = #{paperId};")
    List<PaperQuestionDto> getQuestionsByPaperId(Integer paperId);
}
