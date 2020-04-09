package com.mine.controller;

import com.mine.service.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

@RestController
public class LoginController {

    private static String[] names = {"client1", "client2"};

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam(name = "name") String name) {
        if (Arrays.asList(names).contains(name)) {
            return UUID.randomUUID().toString();
        }
        return "fail";
    }
}
