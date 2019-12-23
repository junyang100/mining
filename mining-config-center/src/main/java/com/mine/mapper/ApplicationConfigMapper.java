package com.mine.mapper;


import com.mine.bean.ApplicationConfig;

import java.util.List;
import java.util.Map;

/**
 * zyp
 */

/**
 * 操作应用配置接口
 */
public interface ApplicationConfigMapper {

    /**
     * 应用启动时，加载配置
     * @param applicationConfig
     * @return
     */
    List<Map<String,Object>> selectAppConfig(ApplicationConfig applicationConfig);

    /**
     * 查询某个应用的平台名称
     * @param application
     * @return
     */
    String selectAppPlatform(String application);


}
