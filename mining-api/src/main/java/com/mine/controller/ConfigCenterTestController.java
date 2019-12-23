package com.mine.controller;

import com.mine.bizService.LoginService;
import com.mine.interceptor.ApiAnnotation;
import com.mine.pojo.UserLoginReq;
import com.mine.vo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigCenterTestController {

    private static Logger logger = LoggerFactory.getLogger(ConfigCenterTestController.class);

    @Value("${com.mining.demo}")
    private String val;

    @ApiAnnotation(needLogin = false, checkSign = false)
    @RequestMapping("/test")
    public void login() {
        System.out.println("========="+val);
    }
}
