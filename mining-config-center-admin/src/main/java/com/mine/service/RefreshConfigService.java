package com.mine.service;



import com.mine.bean.ServiceInfo;

import java.util.List;
import java.util.Map;

/**
 * zyp
 * 刷新配置的服务接口
 */
public interface RefreshConfigService {


    /**
     * 刷新 指定应用的下面的所有机器
     * 的配置
     * @param applicationName 服务名称,平台名称，或者common
     * @param refreshDescription 刷新描述
     */
     void refreshAll(String applicationName, String refreshDescription);


    /**
     * 刷新部分服务器
     * @param applicationName 服务名称,平台名称，或者common
     * @param appNameInfoMap  应用名称 和 服务器map
     * @param refreshDescription 刷新描述
     */

     void refreshPart(String applicationName, Map<String, List<ServiceInfo>> appNameInfoMap, String refreshDescription);



}
