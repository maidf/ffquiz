package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.rep.PaperRep;

public interface PaperMapper extends BaseMapper<Paper> {
    @Select("""
                select
                    p.id,
                    p.name,
                    p.time_limit,
                    p.diff,
                    p.total_score,
                    u.name as creator,
                    p.create_time
                from
                    paper p
                left join
                    user u on u.id = p.creator_id
            """)
    List<PaperRep> selectListPaper();

    @Select("""
                select
                    p.id,
                    p.name,
                    p.time_limit,
                    p.diff,
                    p.total_score,
                    u.name as creator,
                    p.create_time
                from
                    paper p
                left join
                    user u on u.id = p.creator_id
                where
                    p.id = #{paperId}
            """)
    PaperRep selectPaper(Long paperId);
}
