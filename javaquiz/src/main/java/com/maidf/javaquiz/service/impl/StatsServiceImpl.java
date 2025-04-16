package com.maidf.javaquiz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maidf.javaquiz.entity.po.RetryRateStat;
import com.maidf.javaquiz.mapper.StatsMapper;
import com.maidf.javaquiz.service.StatsService;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsMapper statsMapper;

    @Override
    public List<RetryRateStat> ListTop10RateStats() {
        return statsMapper.selectTop10RateStats();
    }

}
