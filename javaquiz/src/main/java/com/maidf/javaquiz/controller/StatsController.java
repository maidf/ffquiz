package com.maidf.javaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.service.StatsService;
import com.maidf.javaquiz.util.Result;

@LoginValidate
@RestController
@RequestMapping("stat")
public class StatsController {
    @Autowired
    private StatsService statsService;

    @GetMapping("retry_rate")
    public ResponseEntity<String> getTop10RetryRateStat() {
        return Result.success(statsService.ListTop10RateStats());
    }

    @GetMapping("bank_qn_nums")
    public ResponseEntity<String> getBankQnNums() {
        return Result.success(statsService.ListBankQnNums());
    }

}
