package com.mine.mapper;


import com.mine.bean.ConfigChangePush;

import java.util.List;

/**
 * 配置推送数据库操作类
 */
public interface ConfigChangePushMapper {


     /**
     *写入数据库记录
     * @param configChangePushe
     * @return
     */
    int persist(ConfigChangePush configChangePushe);

    /**
     * 分页查询总条数
     * @param record
     * @return
     */
    int selectPageCount(ConfigChangePush record);

    /**
     * 查询分页记录数量
     * @param record
     * @return
     */
    List<ConfigChangePush> selectPage(ConfigChangePush record);



}