package com.mine.controller;

import com.mine.pojo.UserLoginReq;
import com.mine.service.MineService;
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

    @RequestMapping("/login")
    public String login() {
        return mineService.login(new UserLoginReq());
    }
}
