package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maidf.javaquiz.entity.po.BankQnNum;
import com.maidf.javaquiz.entity.po.RetryRateStat;
import com.maidf.javaquiz.entity.po.SysStatNum;
import com.maidf.javaquiz.mapper.StatsMapper;
import com.maidf.javaquiz.service.StatsService;

@CacheConfig(cacheNames = "stats_cache")
@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsMapper statsMapper;

    @Cacheable(key = "'top_10_retry_rate'")
    @Override
    public List<RetryRateStat> ListTop10RateStats() {
        return statsMapper.selectTop10RateStats();
    }

    @Cacheable(key = "'bank_qn_nums'")
    @Override
    public List<BankQnNum> ListBankQnNums() {
        return statsMapper.selectBankQnNums();
    }

    @Override
    public SysStatNum getStatNums() {
        return statsMapper.selectSysStatNums();
    }

}
