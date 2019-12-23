package com.mine.controller;

import com.mine.interceptor.ApiAnnotation;
import com.mine.pojo.UserLoginReq;
import com.mine.bizService.LoginService;
import com.mine.vo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @ApiAnnotation(needLogin = false, checkSign = false)
    @RequestMapping("/login")
    public ApiResult login() {
        String rs = loginService.login(new UserLoginReq());
        return ApiResult.success(rs);
    }
}
