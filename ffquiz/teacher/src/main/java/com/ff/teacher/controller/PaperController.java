package com.ff.teacher.controller;

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
import com.ff.common.annotation.CheckOwnerShip;
import com.ff.common.annotation.LoginValidate;
import com.ff.common.entity.dto.PaperDto;
import com.ff.common.entity.enums.EntityTypeEnum;
import com.ff.common.entity.po.Paper;
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
    private JwtUtil jwtUtil;

    @LoginValidate(teacher = false)
    @GetMapping("{id}")
    public ResponseEntity<String> getPaperById(@PathVariable Integer id) {
        return Result.success(paperService.getById(id));
    }

    @LoginValidate(teacher = false)
    @GetMapping
    public ResponseEntity<String> getPapers() {
        return Result.success(paperService.list());
    }

    @PostMapping
    public ResponseEntity<String> createPaper(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer userId = jwtUtil.getLoginUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        Paper paper = mapper.readValue(entity, PaperDto.class).toPaper(userId);

        paperService.save(paper);
        return Result.success();
    }

    @CheckOwnerShip(type = EntityTypeEnum.PAPER)
    @PutMapping("{id}")
    public ResponseEntity<String> updatePaper(@PathVariable Integer id, @RequestBody String entity,
            HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer userId = jwtUtil.getLoginUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        Paper paper = mapper.readValue(entity, PaperDto.class).toPaper(userId);
        paper.setId(id);

        paperService.updateById(paper);
        return Result.success();
    }

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

}
