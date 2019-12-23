package com.mine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class ConfigCenterAdminApplication {

    public static void main(String[] args) {

        SpringApplication.run(ConfigCenterAdminApplication.class, args);
    }

}
