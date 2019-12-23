package com.mine.controller;

import com.mine.bean.Platform;
import com.mine.service.PlatformService;
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
@RequestMapping("/platform")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @RequestMapping("/list")
    public Object list(@RequestParam(value="id",required = false) String id, @RequestParam(required = false) String platform_name, @RequestParam(required = false) String contact_name, @RequestParam(required = true) int page, @RequestParam(required = true) int limit) {
        Platform platform = new Platform();
        platform.setId(id);
        platform.setPlatformName(platform_name);
        platform.setContactName(contact_name);
        platform.setPage(page);
        platform.setLimit(limit);
        return platformService.list(platform);
    }

    @RequestMapping("/listPlatformAndApp")
    public Object listPlatformAndApp(@RequestParam(value="id",required = false) String id, @RequestParam(required = false) String platform_name, @RequestParam(required = false) String contact_name, @RequestParam(required = true) int page, @RequestParam(required = true) int limit) {
        return platformService.listPlatformAndApp();
    }

    @RequestMapping("/add")
    public Object add(@RequestParam(required = true) String id, @RequestParam(required = true) String platform_name, @RequestParam(required = true) String contact_name, @RequestParam(required = true) String contact_mobile, @RequestParam(required = false) String platform_desc){
        Platform platform = new Platform();
        platform.setId(id);
        platform.setPlatformName(platform_name);
        platform.setContactName(contact_name);
        platform.setContactMobile(contact_mobile);
        platform.setPlatformDesc(platform_desc);
        platform.setUpdateTime(new Date());
        return platformService.add(platform);
    }

    @RequestMapping("/update")
    public Object update(@RequestParam(required = true) String id, @RequestParam(required = true) String platform_name, @RequestParam(required = true) String contact_name, @RequestParam(required = true) String contact_mobile, @RequestParam(required = false) String platform_desc){
        Platform platform = new Platform();
        platform.setId(id);
        platform.setPlatformName(platform_name);
        platform.setContactName(contact_name);
        platform.setContactMobile(contact_mobile);
        platform.setPlatformDesc(platform_desc);
        platform.setUpdateTime(new Date());
        return platformService.update(platform);
    }

    @RequestMapping("/delete")
    public Object delete(@RequestParam(required = true) String id){
        return platformService.delete(id);
    }


    /**
     * 查询服务树的结构
     * @return
     */
    @RequestMapping("/queryApplicationTree")
    public Object queryApplicationTree(Integer userId){
        return platformService.queryApplicationTree(userId);
    }


}
