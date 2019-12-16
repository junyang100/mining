package com.mine.share;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShareConfiguration {


    @Bean
    public  PushRefreshResultService pushRefreshResultService(){
        return new PushRefreshResultService();
    }



}
