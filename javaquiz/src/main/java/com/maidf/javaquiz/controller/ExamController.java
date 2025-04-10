package com.maidf.javaquiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.po.Exam;
import com.maidf.javaquiz.entity.req.EndExamReq;
import com.maidf.javaquiz.entity.req.StartAnsReq;
import com.maidf.javaquiz.service.ExamService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SecurityRequirement(name = "jwt")
@CrossOrigin("*")
@LoginValidate
@RestController
@RequestMapping("exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("record")
    public ResponseEntity<String> getExamRecord(HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);
        QueryWrapper<Exam> wrapper = new QueryWrapper<Exam>();
        wrapper.eq("user_id", userId);

        return Result.success(examService.list(wrapper));
    }

    @PostMapping("end")
    public ResponseEntity<String> endExam(@RequestBody List<EndExamReq> ansList, HttpServletRequest req,
            HttpSession session) {
        String token = req.getHeader("exam");
        try {
            examService.endAns(ansList, token);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("start")
    public ResponseEntity<String> startExam(@RequestBody StartAnsReq startAnsReq, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        try {
            String t = examService.startAns(startAnsReq, userId);
            return Result.success(t);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("record/{id}")
    public ResponseEntity<String> deleteAnsRecord(@PathVariable Long id, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("id", id);
        try {
            examService.remove(wrapper);
        } catch (Exception e) {
            return Result.error("找不到记录");
        }
        return Result.success();
    }
}
