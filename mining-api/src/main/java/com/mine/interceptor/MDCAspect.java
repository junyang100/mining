package com.mine.interceptor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Order(1)
public class MDCAspect {

    private static String THREAD_ID = "ThreadID";

    @Pointcut("execution(public * com.mine.controller..*.*(..))")
    public void mdc() {
    }

    @Before("mdc()")
    public void doBefore() throws Throwable {
        MDC.put(THREAD_ID, java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    @AfterReturning(pointcut = "mdc()")
    public void doAfterReturning() throws Throwable {
        MDC.remove(THREAD_ID);
    }

}
