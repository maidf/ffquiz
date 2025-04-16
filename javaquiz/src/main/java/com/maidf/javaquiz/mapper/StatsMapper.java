package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.maidf.javaquiz.entity.po.RetryRateStat;

@Mapper
public interface StatsMapper {
    @Select("""
            select *
            from retry_stats
            order by retry_rate desc
            limit 10
            """)
    List<RetryRateStat> selectTop10RateStats();
}
