package com.mine.config;


import com.mine.service.impl.HttpRefreshConfigServiceImpl;
import com.mine.service.impl.KafkaRefreshConfigServiceImpl;
import com.mine.service.impl.ZKRefreshConfigServiceImpl;
import com.mine.web.SessionFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 用java config 方式配置 spring的
 * bean
 */

@MapperScan("com.mine.mapper")
@Configuration
public class AppConfig {



      @Bean
      public RestTemplate  restTemplate(){

         return new RestTemplate();
      }

      @Bean
      public ExecutorService executorService(){

         ThreadPoolExecutor executor = new ThreadPoolExecutor(5,100,10000, TimeUnit.MICROSECONDS,new LinkedBlockingQueue());
         return executor;

      }


    /**
     * 配置过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionFilter());
        registration.addUrlPatterns("/*");
        registration.setName("sessionFilter");
        return registration;
    }

    /**
     * 创建一个bean
     * @return
     */
    @Bean(name = "sessionFilter")
    public Filter sessionFilter() {
        return new SessionFilter();
    }






    @Configuration
    @ConditionalOnProperty(prefix = "config.refresh",name="type",havingValue = "http")
    public static class EurekaRefreshConfigService{
        @Bean
        public HttpRefreshConfigServiceImpl eurekaRefreshConfigService(){
            return new HttpRefreshConfigServiceImpl();

        }

    }






    @Configuration
    @ConditionalOnProperty(prefix = "config.refresh",name="type",havingValue = "kafka")
    @EnableConfigurationProperties(KafkaProperties.class)
    public static class KafkaRefreshConfigService{
        @Bean
        public KafkaRefreshConfigServiceImpl kafkaRefreshConfigService(){
            return new KafkaRefreshConfigServiceImpl();
        }
    }



    @Configuration
    @EnableConfigurationProperties(ZookeeperProperties.class)
    @ConditionalOnProperty(prefix = "config.refresh",name="type",havingValue = "zk",matchIfMissing = true)
    public static class ZKRefreshConfigServiceConfig{
        @Bean
        public ZKRefreshConfigServiceImpl zkRefreshConfigService(){
            return new ZKRefreshConfigServiceImpl();
        }
    }







}
