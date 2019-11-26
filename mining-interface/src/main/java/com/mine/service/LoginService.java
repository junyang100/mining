package com.mine.service;

import com.mine.config.FeignClientConfig;
import com.mine.pojo.UserLoginReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "${feignclient.mineService.name}", configuration = FeignClientConfig.class)
public interface LoginService {

    @RequestMapping("/login")
    String login(@RequestBody UserLoginReq req);
}
