package com.ff.teacher.controller;

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
import com.ff.common.annotation.CheckOwnerShip;
import com.ff.common.annotation.LoginValidate;
import com.ff.common.entity.dto.AddPaperQuestionDto;
import com.ff.common.entity.dto.PaperDto;
import com.ff.common.entity.enums.EntityTypeEnum;
import com.ff.common.entity.po.Paper;
import com.ff.common.entity.po.PaperQuestion;
import com.ff.common.service.PaperQuestionService;
import com.ff.common.service.PaperService;
import com.ff.common.util.JwtUtil;
import com.ff.common.util.Result;

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
    public ResponseEntity<String> getPaperById(@PathVariable Integer id) {
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
    @GetMapping("{id}/questions")
    public ResponseEntity<String> getPaperQuestions(@PathVariable Integer paperId) {

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
        Integer userId = jwtUtil.getLoginUserId(token);

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
    @PostMapping("question")
    public ResponseEntity<String> addPaperQuestion(@RequestBody List<AddPaperQuestionDto> dtos) {
        List<PaperQuestion> paperQuestions = new ArrayList<>();
        dtos.forEach((d) -> {
            paperQuestions.add(d.toPaperQuestion());
        });
        paperQuestionService.saveBatch(paperQuestions);

        // 更新试卷总分
        Integer paperId = paperQuestions.get(0).getPaperId();
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
    public ResponseEntity<String> updatePaper(@PathVariable Integer id, @RequestBody PaperDto paperDto,
            HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer userId = jwtUtil.getLoginUserId(token);

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
    public ResponseEntity<String> deletePaper(@PathVariable Integer id) {
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
    public ResponseEntity<String> rmPaperQuestion(@PathVariable Integer paperId, @PathVariable Integer questionId) {
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
