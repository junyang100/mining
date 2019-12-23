package com.mine.controller;

import com.mine.bean.ConfigChangePush;
import com.mine.service.ConfigChangePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用配置变更推送controller
 */
@RestController
@RequestMapping("/configChangePush")
public class ConfigChangePushController {

    @Autowired
    private ConfigChangePushService configChangePushService;

    /**
     * 查询应用的配置变更推送信息
     * @param configChangePush
     * @return
     */
    @RequestMapping("/queryConfigChangePushInfo")
    public Object queryConfigChangePushInfo(ConfigChangePush configChangePush){
        return configChangePushService.queryConfigChangePushInfo(configChangePush);
    }





}
