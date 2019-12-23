package com.mine.mapper;


import com.mine.bean.Application;

import java.util.List;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 11:23
 */
public interface ApplicationMapper {

    List<Application> list(Application application);


    int count(Application application);


    int insert(Application applicationBean);


    int deleteByPrimaryKey(String id);


    int updateByPrimaryKey(Application application);

    /**
     * 根据平台查询 服务
     * @param application
     * @return
     */
    List<Application> selectApplicationByPlatform(Application application);

    /**
     * 查询服务名和平台名称相同的服务个数
     * @param applicationName
     * @return
     */
    int selectApplicationIsPlatformCount(String applicationName);

}
