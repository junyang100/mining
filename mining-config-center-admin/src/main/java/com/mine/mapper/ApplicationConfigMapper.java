package com.mine.mapper;

import com.mine.bean.ApplicationConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * zyp
 */

/**
 * 操作应用配置接口
 */
public interface ApplicationConfigMapper {


    /**
     * 分页查询配置信息
     * @param config
     * @return
     */
    List<ApplicationConfig> list(ApplicationConfig config);

    /**
     * 查询分页的条数
     * @param config
     * @return
     */
    int count(ApplicationConfig config);

    /**
     * 添加应用配置
     * @param applicationConfig
     * @return
     */
    int add(ApplicationConfig applicationConfig);

    /**
     * 删除应用配置
     * @param config
     * @return
     */
    int delete(ApplicationConfig config);

    /**
     * 更新应用配置
     * @param config
     * @return
     */
    int update(ApplicationConfig config);

    /**
     * 复制某个应用某个版本到
     * 另一个版本的配置
     * @param createTime
     * @param newLabel
     * @param profile
     * @param application
     * @param oldLabel
     * @return
     */
    int duplicate(@Param("createTime") Date createTime, @Param("newLabel") String newLabel,
                  @Param("profile") String profile,
                  @Param("application") String application, @Param("oldLabel") String oldLabel);


    /**
     * 查询某个应用从配置中心获取的配置
     * @param applicationConfig
     * @return
     */
    List<ApplicationConfig> selectAppUsedConfig(ApplicationConfig applicationConfig);

    /**
     * 查询应用所属的平台
     * @param application
     * @return
     */
    String selectAppPlatform(String application);

}
