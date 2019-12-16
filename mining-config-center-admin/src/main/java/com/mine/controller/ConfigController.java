package com.mine.controller;

import com.mine.bean.ApplicationConfig;
import com.mine.service.ApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 10:48
 * 配置文件管理
 */

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ApplicationConfigService configService;

    @RequestMapping("/list")
    public Object list(@RequestParam(value="application12",required = false) String application,
                       @RequestParam(value="key12",required = false) String key,
                       @RequestParam(value="config_group12",required = false) String configGroup,
                       @RequestParam(value="profile12",required = false) String profile,
                       @RequestParam String page,
                       @RequestParam(value = "limit") String pageSize){
        ApplicationConfig config = new ApplicationConfig();
        config.setPage(Integer.parseInt(page));
        config.setLimit(Integer.parseInt(pageSize));
        config.setApplication(application);
        config.setKey(key);
        config.setConfigGroup(configGroup);
        config.setProfile(profile);
       return  configService.list(config);
    }

    @RequestMapping("/add")
    public Object add(@RequestParam(value="application",required = false) String application,
                      @RequestParam(value="profile",required = false) String profile,
                      @RequestParam(value="label",required = false) String label,
                      @RequestParam(value="key",required = false) String key,
                      @RequestParam(value="value",required = false) String value,
                      @RequestParam(value="description",required = false) String description,
                      @RequestParam(value="profile_desc",required = false) String profile_desc,
                      @RequestParam(value="label_desc",required = false) String label_desc,
                      @RequestParam(value="config_group",required = false) String config_group
                      ){
        ApplicationConfig config = new ApplicationConfig();
        config.setApplication(application.trim());
        config.setProfile(profile.trim());
        config.setLabel(label.trim());
        config.setKey(key.trim());
        config.setValue(value.trim());
        config.setDescription(description);
        config.setProfileDesc(profile_desc);
        config.setConfigGroup(config_group);
        config.setLabelDesc(label_desc);
        return configService.add(config);
    }

    @RequestMapping("/update")
    public Object update(@RequestParam(value="application",required = false)
                                     String application,
                         @RequestParam(value="profile",required = false)
                                     String profile,
                         @RequestParam(value="label",required = false) String label,
                         @RequestParam(value="key",required = false) String key,
                         @RequestParam(value="value",required = false) String value,
                         @RequestParam(value="description",required = false) String description,
                         @RequestParam(value="id",required = false) String id,
                         @RequestParam(value="profile_desc",required = false) String profile_desc,
                         @RequestParam(value="label_desc",required = false) String label_desc,
                         @RequestParam(value="config_group",required = false) String config_group,
                         @RequestParam(value="flag",required = false) String flag,
                         @RequestParam(value="modifyDescription",required = false) String modifyDescription,
                         @RequestParam(value="ipPorts",required = false) String ipPorts
                       ){
        ApplicationConfig config = new ApplicationConfig();
        config.setApplication(application.trim());
        config.setProfile(profile.trim());
        config.setLabel(label.trim());
        config.setKey(key.trim());
        config.setValue(value.trim());
        config.setDescription(description);
        config.setProfileDesc(profile_desc);
        config.setConfigGroup(config_group);
        config.setLabelDesc(label_desc);
        config.setId(Integer.parseInt(id));
        return configService.update(config,flag,ipPorts,modifyDescription);

    }

    @RequestMapping("/delete")
    public Object delete(@RequestParam(value="id",required = false) String id){
        Integer ids  = null;
        if(!"".equals(id) && id != null){
            ids = Integer.parseInt(id);
        }
        ApplicationConfig config = new ApplicationConfig();
        config.setId(ids);
        return configService.delete(config);
    }

    /**
     * 复制一个应用某个版本的所有属性，用来上线使用
     * @return
     */
   @RequestMapping("/copyApplicationProperties")
   public Object  copyApplicationProperties(@RequestParam Map<String,String> params){
       return configService.copyApplicationProperties(params);
   }

   /**
     * 查询某个应用使用的配置信息
     * @return
     */
   @RequestMapping(value = "/listUsedConfigs")
   public Object  queryApplicationApplyConfigs(ApplicationConfig applicationConfig){
       return configService.queryApplicationUsedConfigs(applicationConfig);
   }






}
