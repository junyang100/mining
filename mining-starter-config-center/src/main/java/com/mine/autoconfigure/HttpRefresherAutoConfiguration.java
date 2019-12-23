package com.mine.autoconfigure;


import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * zyp
 * 提供http的方式刷新配置
 */
@ConditionalOnProperty(prefix = "config.refersh",name = "type",havingValue = "http")
@ConditionalOnBean(ContextRefresher.class)
@Configuration
public class HttpRefresherAutoConfiguration {


    @Bean
    public CustomRefreshMvcEndpoint customRefreshEndpoint(){
        return new CustomRefreshMvcEndpoint();
    }


}
