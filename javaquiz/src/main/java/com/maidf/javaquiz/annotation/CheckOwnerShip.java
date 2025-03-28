package com.maidf.javaquiz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.maidf.javaquiz.entity.enums.EntityTypeEnum;

/**
 * 题库操作验证
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckOwnerShip {
    EntityTypeEnum type() default EntityTypeEnum.BANK;
}
