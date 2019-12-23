package com.mine.mapper;


import com.mine.bean.Platform;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 11:23
 */

public interface PlatformMapper {


    List<Platform> list(Platform platform);

    int count(Platform platform);

    List<Map<String, Object>> listPlatformAndApp(Set<String> applications);

    int insert(Platform platform);

    int deleteByPrimaryKey(String id);

    int updateByPrimaryKey(Platform platformBean);

    List<Platform> selectApplicationTree();

}
