package com.mine.controller;

import com.mine.bean.Application;
import com.mine.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author guoyangyang
 *         Date: 2018/1/4
 *         Time: 10:48
 *         配置文件管理
 */

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping("/list")
    public Object list(@RequestParam(value="id",required = false) String id, @RequestParam(required = false) String application_name, @RequestParam(required = false) String platform, @RequestParam(required = true) int page, @RequestParam(required = true) int limit) {
        Application application = new Application();
        application.setId(id);
        application.setApplicationName(application_name);
        application.setPlatform(platform);
        application.setPage(page);
        application.setLimit(limit);
        return applicationService.list(application);
    }

    @RequestMapping("/add")
    public Object add(@RequestParam(required = true) String id, @RequestParam(required = true) String application_name, @RequestParam(required = false) String application_desc, @RequestParam(required = true) String platform){
        Application application = new Application();
        application.setId(id);
        application.setApplicationName(application_name);
        application.setApplicationDesc(application_desc);
        application.setPlatform(platform);
        application.setUpdateTime(new Date());
        return applicationService.add(application);
    }

    @RequestMapping("/update")
    public Object update(@RequestParam(required = true) String id, @RequestParam(required = true) String application_name, @RequestParam(required = false) String application_desc, @RequestParam(required = true) String platform){
        Application applicationBean = new Application();
        applicationBean.setId(id);
        applicationBean.setApplicationName(application_name);
        applicationBean.setApplicationDesc(application_desc);
        applicationBean.setPlatform(platform);
        applicationBean.setUpdateTime(new Date());
        return applicationService.update(applicationBean);
    }

    @RequestMapping("/delete")
    public Object delete(@RequestParam(required = true) String id){
        return applicationService.delete(id);
    }

}
