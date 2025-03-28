package com.maidf.javaquiz.controller;

import java.util.ArrayList;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maidf.javaquiz.annotation.CheckOwnerShip;
import com.maidf.javaquiz.annotation.LoginValidate;
import com.maidf.javaquiz.entity.dto.AddPaperQuestionDto;
import com.maidf.javaquiz.entity.dto.PaperDto;
import com.maidf.javaquiz.entity.enums.EntityTypeEnum;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.PaperQuestion;
import com.maidf.javaquiz.service.PaperQuestionService;
import com.maidf.javaquiz.service.PaperService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

import jakarta.servlet.http.HttpServletRequest;

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
        return Result.success(paperService.getById(id));
    }

    /**
     * 获取所有试卷信息
     * 
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping
    public ResponseEntity<String> getPapers() {
        return Result.success(paperService.list());
    }

    /**
     * 获取试卷的所有问题
     * 
     * @param paperId
     * @return
     */
    @LoginValidate(teacher = false)
    @GetMapping("{paperId}/questions")
    public ResponseEntity<String> getPaperQuestions(@PathVariable Long paperId) {

        return Result.success(paperService.listQuestions(paperId));
    }

    /**
     * 创建试卷
     * 
     * @param paperDto
     * @param req
     * @return
     */
    @PostMapping
    public ResponseEntity<String> createPaper(@RequestBody PaperDto paperDto, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        Paper paper = paperDto.toPaper(userId);

        paperService.save(paper);
        return Result.success();
    }

    /**
     * 添加试卷题目
     * 
     * @param dtos
     * @return
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @PostMapping("{paperId}/questions")
    public ResponseEntity<String> addPaperQuestion(@PathVariable Long paperId, @RequestBody List<AddPaperQuestionDto> dtos) {
        List<PaperQuestion> paperQuestions = new ArrayList<>();
        dtos.forEach((d) -> {
            paperQuestions.add(d.toPaperQuestion(paperId));
        });
        paperQuestionService.saveBatch(paperQuestions);

        // 更新试卷总分
        Integer addTotalScore = 0;
        for (PaperQuestion paperQuestion : paperQuestions) {
            addTotalScore += paperQuestion.getScore();
        }
        Paper paper = paperService.getById(paperId);
        paper.setTotalScore(paper.getTotalScore() + addTotalScore);
        paperService.updateById(paper);

        return Result.success();
    }

    /**
     * 修改试卷信息
     * 
     * @param id
     * @param paperDto
     * @param req
     * @return
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @PutMapping("{id}")
    public ResponseEntity<String> updatePaper(@PathVariable Long id, @RequestBody PaperDto paperDto,
            HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        Paper paper = paperDto.toPaper(userId);
        paper.setId(id);

        paperService.updateById(paper);
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
     * 删除试卷题目
     * 
     * @param paperId
     * @param questionId
     * @return
     */
    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @DeleteMapping("{paperId}/question/{questionId}")
    public ResponseEntity<String> rmPaperQuestion(@PathVariable Long paperId, @PathVariable Long questionId) {
        QueryWrapper<PaperQuestion> wrapper = new QueryWrapper<PaperQuestion>();
        wrapper.eq("paper_id", paperId).eq("question_id", questionId);

        Integer minusTotalScore = 0;
        minusTotalScore += paperQuestionService.getOne(wrapper).getScore();

        paperQuestionService.remove(wrapper);

        // 更新试卷总分
        Paper paper = paperService.getById(paperId);
        paper.setTotalScore(paper.getTotalScore() - minusTotalScore);
        paperService.updateById(paper);
        return Result.success();
    }

}
