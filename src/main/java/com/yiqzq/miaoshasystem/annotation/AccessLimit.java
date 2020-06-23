package com.yiqzq.miaoshasystem.annotation;

import java.lang.annotation.*;

/**
 * @author yiqzq
 * @date 2020/6/18 17:05
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    int limit() default 5;
    int sec() default 5;
}