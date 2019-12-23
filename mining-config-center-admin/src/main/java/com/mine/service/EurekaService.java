package com.mine.service;


import com.mine.bean.ServiceInfo;

import java.util.List;
import java.util.Map;

/**
 * 封装操作Eureka的方法
 */
public interface EurekaService {

    /**
     * 查询某个应用的运行实例  信息
     * @param applicationName
     * @return  key appname  value 应用列表
     */
    Map<String, List<ServiceInfo>> queryPlatformOrCommonOrAppInfo(String applicationName);



}
