package com.maidf.javaquiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maidf.javaquiz.annotation.CheckOwnerShip;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.dto.BankDto;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.service.QuestionBankService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpServletRequest;

@LoginValidate(teacher = true)
@RestController
@RequestMapping("question")
public class QuestionBankController {

    @Autowired
    private QuestionBankService bankService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("bank")
    public ResponseEntity<String> createBank(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        QuestionBank bank = mapper.readValue(entity, BankDto.class).toBank(userId);

        bankService.save(bank);
        return Result.success();
    }

    @CheckOwnerShip
    @PutMapping("bank/{id}")
    public ResponseEntity<String> updateBank(@PathVariable Long id, @RequestBody String entity,
            HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        QuestionBank bank = mapper.readValue(entity, BankDto.class).toBank(userId);
        bank.setId(id);

        bankService.updateById(bank);
        return Result.success();
    }

    @CheckOwnerShip
    @DeleteMapping("bank/{id}")
    public ResponseEntity<String> deleteBank(@PathVariable Long id) {
        try {
            bankService.rmById(id);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    @LoginValidate(teacher = false)
    @GetMapping("bank")
    public ResponseEntity<String> getBanks() {
        List<QuestionBank> banks = bankService.list();
        return Result.success(banks);
    }

    @LoginValidate(teacher = false)
    @GetMapping("bank/{id}")
    public ResponseEntity<String> getBank(@PathVariable Long id) {
        QuestionBank bank = bankService.getById(id);
        return Result.success(bank);
    }

}
