package com.mine.share;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.cloud.config",name = "enabled",havingValue = "true",matchIfMissing = true)
@EnableDiscoveryClient
@EnableConfigurationProperties({ConfigClientProperties.class,RetryProperties.class})
public class LocalCachePropertyLocatorBootstrapConfiguration {

    @Bean
    public LocalCachePropertySourceLocator  localCachePropertySourceLocator(){
        return new LocalCachePropertySourceLocator();
    }


    @Bean
    @ConditionalOnProperty(name = "spring.cloud.config.discovery.enabled",havingValue = "true",matchIfMissing = false)
    public EurekaService eurekaService(){

        return new EurekaService();
    }


    @Bean
    public ConfigServiceURLProvider configServiceURLProvider(){
        return new ConfigServiceURLProvider();
    }


}
