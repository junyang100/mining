package com.mine.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "config.refersh",name = "type",havingValue = "kafka")
@ConditionalOnBean(ContextRefresher.class)
@EnableConfigurationProperties(KafkaConsumerProperties.class)
@Configuration
public class KafkaRefresherAutoConfiguration  {

    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    @ConditionalOnBean(ContextRefresher.class)
    @Bean(destroyMethod = "close")
    public KafkaRefresher kafkaRefresher(ContextRefresher contextRefresher){
            KafkaRefresher kafkaRefresher = new KafkaRefresher(contextRefresher,kafkaConsumerProperties);
            Thread refreshThread = new Thread(kafkaRefresher,"Refresh-config-Thread");
            refreshThread.setDaemon(true);
            refreshThread.start();
            return kafkaRefresher;
    }



}
