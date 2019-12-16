package com.mine.service.impl;

import com.mine.bean.ConfigChangePush;
import com.mine.bean.JsonResult;
import com.mine.bean.ServiceInfo;
import com.mine.mapper.ConfigChangePushMapper;
import com.mine.service.ConfigChangePushService;
import com.mine.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConfigChangePushServcieImpl implements ConfigChangePushService {

    @Autowired
    private ConfigChangePushMapper configChangePushMapper;

    @Override
    public JsonResult<List<ConfigChangePush>> queryConfigChangePushInfo(ConfigChangePush configChangePush) {
        int count = configChangePushMapper.selectPageCount(configChangePush);
        List<ConfigChangePush> list = null;
        if(count > 0){
            list = configChangePushMapper.selectPage(configChangePush);
        }
        return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,list,count);

    }

    @Override
    public Map<String,Integer> getPushData(Collection<ServiceInfo> serviceInfos,String refreshDescription) {
        Map<String,Integer> pushIpPorts = new HashMap((int)(serviceInfos.size() / 0.75f));
        for(ServiceInfo serviceInfo : serviceInfos){
            ConfigChangePush configChangePush = new ConfigChangePush();
            configChangePush.setApplication(serviceInfo.getApplicationName());
            configChangePush.setApplicationIp(serviceInfo.getIp());
            configChangePush.setApplicationPort(serviceInfo.getPort());
            configChangePush.setPushStatus(Byte.valueOf("1"));
            configChangePush.setPushTime(new Date());
            configChangePush.setPushUserName(WebUtils.getUserFromSession().getNickName());
            configChangePush.setPushDescription(refreshDescription);
            configChangePushMapper.persist(configChangePush);
            pushIpPorts.put(serviceInfo.getIp() + ":" +serviceInfo.getPort(),configChangePush.getId().intValue());
        }
        return pushIpPorts;
    }
}
