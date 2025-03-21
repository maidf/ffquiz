package com.ff.common.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ff.common.annotation.LoginValidate;
import com.ff.common.util.JwtUtil;
import com.ff.common.util.Result;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginValiAspect {

    @Autowired
    private JwtUtil jwtUtil;

    @Pointcut("@annotation(com.ff.common.annotation.LoginValidate) || @within(com.ff.common.annotation.LoginValidate)")
    public void loginValidate() {
    }

    @Order(1)
    @Around("loginValidate()")
    public Object login_validate(ProceedingJoinPoint join_point) throws Throwable {
        // 检查是否需要验证
        Method method = ((MethodSignature) join_point.getSignature()).getMethod();
        LoginValidate validate = method.getAnnotation(LoginValidate.class);

        // 如果方法上没有注解，再尝试获取类上的 @LoginValidate 注解
        if (validate == null) {
            Class<?> targetClass = method.getDeclaringClass();
            validate = targetClass.getAnnotation(LoginValidate.class);
        }

        if (validate != null && !validate.login()) {
            return join_point.proceed(join_point.getArgs());
        }

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        if (jwtUtil.validateToken(token)) {
            return join_point.proceed(join_point.getArgs());
        } else {
            return Result.error("没有登录");
        }
    }

    @Order(2)
    @Around("loginValidate()")
    public Object admin_validate(ProceedingJoinPoint join_point) throws Throwable {
        // 检查是否需要验证
        Method method = ((MethodSignature) join_point.getSignature()).getMethod();
        LoginValidate validate = method.getAnnotation(LoginValidate.class);

        // 如果方法上没有注解，再尝试获取类上的 @LoginValidate 注解
        if (validate == null) {
            Class<?> targetClass = method.getDeclaringClass();
            validate = targetClass.getAnnotation(LoginValidate.class);
        }

        if (validate != null && !validate.teacher()) {
            return join_point.proceed(join_point.getArgs());
        }

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        if (jwtUtil.verifyIdentity(token)) {
            return join_point.proceed(join_point.getArgs());
        } else {
            return Result.error("没有权限");
        }

    }
}
