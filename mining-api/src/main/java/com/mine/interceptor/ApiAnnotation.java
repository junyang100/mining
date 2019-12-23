package com.mine.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 统一拦截注解（签名校验、参数转换、统一日志、返回结果签名）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiAnnotation {

    /**
     * 是否校验签名
     * @return
     */
    boolean checkSign() default true;


    /**
     * 是否需要传递head
     * @return
     */
    boolean needLogin() default true;
}
