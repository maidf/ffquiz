package com.ff.common.entity.po;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("paper_question")
public class PaperQuestion {
    private Integer paperId;
    private Integer questionId;
    private Integer score;
}
