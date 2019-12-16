package com.mine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.mine.*"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mine.*"})
public class MineWalletHdApplication {
    public static void main(String[] args) {
        SpringApplication.run(MineWalletHdApplication.class, args);
    }
}
