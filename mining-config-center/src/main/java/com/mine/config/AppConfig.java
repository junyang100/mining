package com.mine.config;


import com.mine.extend.ExtendedJdbcEnvironmentRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 用java config 方式配置 spring的
 * bean
 */

@MapperScan("com.mine.mapper")
@Configuration
public class AppConfig {

      @Bean
      public ExtendedJdbcEnvironmentRepository extendedJdbcEnvironmentRepository(){

          return new ExtendedJdbcEnvironmentRepository();
      }




}
