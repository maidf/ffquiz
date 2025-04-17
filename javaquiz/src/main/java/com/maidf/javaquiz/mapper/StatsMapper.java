package com.maidf.javaquiz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.maidf.javaquiz.entity.po.BankQnNum;
import com.maidf.javaquiz.entity.po.RetryRateStat;
import com.maidf.javaquiz.entity.po.SysStatNum;

@Mapper
public interface StatsMapper {
    @Select("""
            select *
            from retry_stats
            order by retry_rate desc
            limit 10
            """)
    List<RetryRateStat> selectTop10RateStats();

    @Select("""
            select *
            from bank_qn_num_view
            order by qn_nums desc
            limit 10
            """)
    List<BankQnNum> selectBankQnNums();

    @Select("""
            select *
            from sys_stats
            """)
    SysStatNum selectSysStatNums();
}
