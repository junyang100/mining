package com.mine.service;


import com.mine.bean.ConfigChangePush;
import com.mine.bean.JsonResult;
import com.mine.bean.ServiceInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 配置变更推送服务接口
 */
public interface ConfigChangePushService {


    /**
     * 根据条件查询应用配置修改推送信息
     * @param configChangePush
     * @return
     */
    JsonResult<List<ConfigChangePush>> queryConfigChangePushInfo(ConfigChangePush configChangePush);


    /**
     * 获取http刷新方式配置变更的时候获取ip数据
     * @return
     */
    Map<String,Integer> getPushData(Collection<ServiceInfo> serviceInfoList, String refreshDescription);


}
