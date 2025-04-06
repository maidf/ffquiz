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
import com.maidf.javaquiz.entity.enums.EntityTypeEnum;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.service.PaperQuestionService;
import com.maidf.javaquiz.service.PaperService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@SecurityRequirement(name = "jwt")
@CrossOrigin("*")
@LoginValidate(teacher = true)
@RestController
@RequestMapping("paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取指定试卷信息
     * 
     * @param id
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping("{id}")
    public ResponseEntity<String> getPaperById(@PathVariable Long id) {
        return Result.success(paperService.getPaperById(id));
    }

    /**
     * 获取所有试卷信息
     * 
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping
    public ResponseEntity<String> getPapers() {
        return Result.success(paperService.listPapers());
    }

    /**
     * 获取试卷的所有问题
     * 
     * @param paperId
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping("{paperId}/qs")
    public ResponseEntity<String> getPaperQuestions(@PathVariable Long paperId) {

        return Result.success(paperService.listQs(paperId));
    }

    /**
     * 创建试卷
     * 
     */
    @PostMapping
    public ResponseEntity<String> createPaper(@RequestBody Paper paper, HttpServletRequest req) {
        String token = req.getHeader(jwtUtil.getHeader());
        Long userId = jwtUtil.getLoginUserId(token);

        paper.setCreatorId(userId);

        paperService.savePaper(paper);
        return Result.success();
    }

    /**
     * 添加试卷题目
     * 
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @PostMapping("{paperId}/qs")
    public ResponseEntity<String> addPaperQuestion(@PathVariable Long paperId,
            @RequestBody List<PaperQuestion> pQs) {

        paperQuestionService.saveBatchQn(paperId, pQs);

        return Result.success();
    }

    /**
     * 修改试卷信息
     * 
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @PutMapping("{id}")
    public ResponseEntity<String> updatePaper(@PathVariable Long id, @RequestBody Paper paper) {
        paper.setId(id);

        paperService.updatePaper(paper);
        return Result.success();
    }

    /**
     * 删除试卷
     * 
     * @param id
     * @return
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePaper(@PathVariable Long id) {
        try {
            paperService.rmById(id);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**
     * 移除试卷题目
     * 
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @DeleteMapping("{paperId}/qn/{qnId}")
    public ResponseEntity<String> rmPaperQuestion(@PathVariable Long paperId, @PathVariable Long qnId) {
        paperQuestionService.rmQn(paperId, qnId);
        return Result.success();
    }

}
