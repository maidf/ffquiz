package com.maidf.javaquiz.aspect;

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

import com.maidf.javaquiz.annotation.CheckOwnerShip;
import com.maidf.javaquiz.entity.enums.EntityTypeEnum;
import com.maidf.javaquiz.entity.po.Paper;
import com.maidf.javaquiz.entity.po.Question;
import com.maidf.javaquiz.entity.po.QuestionBank;
import com.maidf.javaquiz.service.PaperService;
import com.maidf.javaquiz.service.QuestionBankService;
import com.maidf.javaquiz.service.QuestionService;
import com.maidf.javaquiz.util.JwtUtil;
import com.maidf.javaquiz.util.Result;

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

    @Pointcut("@annotation(com.maidf.javaquiz.annotation.CheckOwnerShip) || @within(com.maidf.javaquiz.annotation.CheckOwnerShip)")
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
        Long entityId = (Long) args[0];

        // 获取访问用户ID
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        Long userId = jwtUtil.getLoginUserId(token);

        try {
            switch (entityType) {
                case EntityTypeEnum.PAPER:
                    checkPaperOwnerShip(userId, entityId);
                    break;
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

    private void checkPaperOwnerShip(Long userId, Long paperId) throws Exception {
        Paper paper = paperService.getById(paperId);
        if (paper == null) {
            throw new Exception("找不到试卷");
        }
        if (!userId.equals(paper.getCreatorId())) {
            throw new Exception("没有权限");
        }
    }

    private void checkQuestionOwnerShip(Long userId, Long questionId) throws Exception {
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new Exception("找不到题目");
        }
        if (!userId.equals(question.getCreatorId())) {
            throw new Exception("没有权限");
        }
        checkQuestionBankOwnerShip(userId, question.getBankId());
    }

    private void checkQuestionBankOwnerShip(Long userId, Long questionBankId) throws Exception {
        QuestionBank bank = questionBankService.getById(questionBankId);
        if (bank == null) {
            throw new Exception("找不到题库");
        }
        if (!userId.equals(bank.getCreatorId())) {
            throw new Exception("没有权限");
        }
    }
}
