package com.mine.impl;

import com.mine.pojo.UserLoginReq;
import com.mine.service.LoginService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginServiceImpl implements LoginService {

    @Override
    public String login(UserLoginReq req) {
        return "login success";
    }
}
