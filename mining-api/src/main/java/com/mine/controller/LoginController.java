package com.mine.controller;

import com.mine.interceptor.ApiAnnotation;
import com.mine.pojo.UserLoginReq;
import com.mine.service.MineService;
import com.mine.vo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MineService mineService;

    @ApiAnnotation(paramType = UserLoginReq.class)
    @RequestMapping("/login")
    public ApiResult login() {
        String rs = mineService.login(new UserLoginReq());
        return ApiResult.success(rs);
    }
}
