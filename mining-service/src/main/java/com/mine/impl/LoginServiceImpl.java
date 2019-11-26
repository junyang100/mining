package com.mine.impl;

import com.mine.mapper.UserMapper;
import com.mine.pojo.UserLoginReq;
import com.mine.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String login(UserLoginReq req) {
        return userMapper.selectCount() + "";
    }
}
