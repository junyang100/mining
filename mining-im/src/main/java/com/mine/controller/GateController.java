package com.mine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class GateController {

    private static Logger logger = LoggerFactory.getLogger(GateController.class);

    @RequestMapping("/gate")
    @ResponseBody
    public Object login(@RequestParam(name = "name") String name) {
        Map map = new HashMap();
        map.put("ip", "127.0.0.1");
        map.put("port", "8888");
        return map;
    }
}
