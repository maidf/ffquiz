package com.maidf.javaquiz.service;

import java.util.List;

import com.maidf.javaquiz.entity.po.BankQnNum;
import com.maidf.javaquiz.entity.po.RetryRateStat;

public interface StatsService {
    List<RetryRateStat> ListTop10RateStats();

    List<BankQnNum> ListBankQnNums();
}
