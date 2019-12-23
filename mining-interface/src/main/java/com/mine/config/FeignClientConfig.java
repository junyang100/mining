package com.mine.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;


public class FeignClientConfig {

//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }

    @Bean
    public RequestInterceptor interceptor() {
        return requestTemplate -> {
            requestTemplate.header("ThreadID", MDC.get("ThreadID"));
        };
    }
}
