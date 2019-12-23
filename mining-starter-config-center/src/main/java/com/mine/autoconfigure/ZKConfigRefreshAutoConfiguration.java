package com.mine.autoconfigure;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ContextRefresher.class)
@EnableConfigurationProperties(ZKProperties.class)
@ConditionalOnProperty(prefix = "config.refersh",name = "type",havingValue = "zk",matchIfMissing = true)
public class ZKConfigRefreshAutoConfiguration {


    @Value("${spring.application.name}")
    private String applicationName;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public ZKRefresher zkRefresher(ZKProperties zookeeperProperties, ContextRefresher contextRefresher){
        CuratorFramework curatorFramework = null;
        try{
             curatorFramework = CuratorFrameworkFactory.builder().
                    connectString(zookeeperProperties.getConnectString())
                    .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
                    .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
                    .namespace(zookeeperProperties.getNamespace())
                    .retryPolicy(new ExponentialBackoffRetry(1000,zookeeperProperties.getMaxRetryCount()))
                    .build();
            curatorFramework.start();
        }catch(Exception e){
            logger.warn("refresh zookeeper init error,dynamic cofig not used",e);
        }

        ZKRefresher zkRefresher = new ZKRefresher(contextRefresher,curatorFramework, applicationName);
        zkRefresher.start();
        logger.info(zookeeperProperties.toString());
        return zkRefresher;
    }




}
