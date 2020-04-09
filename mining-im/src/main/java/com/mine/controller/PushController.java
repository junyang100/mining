package com.mine.controller;

import com.mine.service.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushController {

    private static Logger logger = LoggerFactory.getLogger(PushController.class);

    @RequestMapping("/push")
    @ResponseBody
    public String login(@RequestParam(name = "msg")String msg) {
        ServerHandler.sendMsg(msg);
        return "success";
    }
}
