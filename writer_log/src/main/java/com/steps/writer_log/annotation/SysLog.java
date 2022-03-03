package com.steps.writer_log.annotation;

import java.lang.annotation.*;

/**
 * 记录操作日志注解
 *
 * @author fx
 * @date 2021-05-20 10:57
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String title() default "";

    //接口描述
    String value();

}
