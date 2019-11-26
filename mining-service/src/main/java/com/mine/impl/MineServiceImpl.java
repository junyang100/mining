package com.mine.impl;

import com.mine.pojo.UserLoginReq;
import com.mine.service.MineService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MineServiceImpl implements MineService {

    @Override
    public String login(UserLoginReq req) {
        return "login success";
    }
}
