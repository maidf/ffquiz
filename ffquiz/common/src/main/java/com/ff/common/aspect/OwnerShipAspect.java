package com.ff.common.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ff.common.annotation.CheckOwnerShip;
import com.ff.common.entity.enums.EntityTypeEnum;
import com.ff.common.entity.po.Paper;
import com.ff.common.entity.po.Question;
import com.ff.common.entity.po.QuestionBank;
import com.ff.common.service.PaperService;
import com.ff.common.service.QuestionBankService;
import com.ff.common.service.QuestionService;
import com.ff.common.util.JwtUtil;
import com.ff.common.util.Result;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class OwnerShipAspect {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PaperService paperService;

    @Pointcut("@annotation(com.ff.common.annotation.CheckOwnerShip) || @within(com.ff.common.annotation.CheckOwnerShip)")
    public void checkOwnerShip() {
    }

    @Around("checkOwnerShip()")
    public Object checkOwnerShip(ProceedingJoinPoint joinPoint) throws Throwable {
        // 检查是否需要验证
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        CheckOwnerShip anno = method.getAnnotation(CheckOwnerShip.class);

        // 如果方法上没有注解，再尝试获取类上的 @LoginValidate 注解
        if (anno == null) {
            Class<?> targetClass = method.getDeclaringClass();
            anno = targetClass.getAnnotation(CheckOwnerShip.class);
        }

        // 获取注解判断类型
        EntityTypeEnum entityType = anno.type();

        // 获取题目/题库/试卷ID
        Object[] args = joinPoint.getArgs();
        Integer entityId = (Integer) args[0];

        // 获取访问用户ID
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        Integer userId = jwtUtil.getLoginUserId(token);

        try {
            switch (entityType) {
                case EntityTypeEnum.PAPER:
                    checkPaperOwnerShip(userId, entityId);
                case EntityTypeEnum.QUESTION:
                    checkQuestionOwnerShip(userId, entityId);
                    break;
                default:
                    checkQuestionBankOwnerShip(userId, entityId);
                    break;
            }

            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }

    }

    private void checkPaperOwnerShip(Integer userId, Integer paperId) throws Exception {
        Paper paper = paperService.getById(paperId);
        if (paper == null) {
            throw new Exception("找不到试卷");
        }
        if (!userId.equals(paper.getCreatorId())) {
            throw new Exception("没有权限");
        }
    }

    private void checkQuestionOwnerShip(Integer userId, Integer questionId) throws Exception {
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new Exception("找不到题目");
        }
        if (!userId.equals(question.getCreatorId())) {
            throw new Exception("没有权限");
        }
        checkQuestionBankOwnerShip(userId, question.getBankId());
    }

    private void checkQuestionBankOwnerShip(Integer userId, Integer questionBankId) throws Exception {
        QuestionBank bank = questionBankService.getById(questionBankId);
        if (bank == null) {
            throw new Exception("找不到题库");
        }
        if (!userId.equals(bank.getCreatorId())) {
            throw new Exception("没有权限");
        }
    }
}
