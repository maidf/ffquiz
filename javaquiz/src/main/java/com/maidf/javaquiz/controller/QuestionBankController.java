package com.maidf.javaquiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maidf.javaquiz.annotation.CheckOwnerShip;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.entity.rep.BankRep;
import com.maidf.javaquiz.service.QuestionBankService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@SecurityRequirement(name = "jwt")
@CrossOrigin("*")
@LoginValidate(teacher = true)
@RestController
@RequestMapping("bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService bankService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 添加题库
     * 
     */
    @PostMapping
    public ResponseEntity<String> createBank(@RequestBody QuestionBank bank, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        bank.setCreatorId(userId);

        bankService.save(bank);
        return Result.success();
    }

    /**
     * 修改题库
     */
    @CheckOwnerShip
    @PutMapping("{id}")
    public ResponseEntity<String> updateBank(@PathVariable Long id, @RequestBody QuestionBank bank,
            HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        bank.setCreatorId(userId);
        bank.setId(id);

        bankService.updateById(bank);
        return Result.success();
    }

    /**
     * 删除指定题库
     * 
     * @param id
     * @return
     */
    @CheckOwnerShip
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBank(@PathVariable Long id) {
        try {
            bankService.rmById(id);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**
     * 获取所有题库
     * 
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping("all")
    public ResponseEntity<String> getBanks() {

        List<BankRep> banks = bankService.listBanks();

        return Result.success(banks);
    }

    /**
     * 获取指定题库信息
     * 
     * @param id
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping("{id}")
    public ResponseEntity<String> getBank(@PathVariable Long id) {
        BankRep bank = bankService.getBankById(id);
        return Result.success(bank);
    }

}
