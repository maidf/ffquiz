package com.maidf.javaquiz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录验证
 *
 * @param login 默认 true 开启验证
 * @param auth  默认 false 不开启验证
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginValidate {
    boolean login() default true;

    boolean teacher() default false;
}
