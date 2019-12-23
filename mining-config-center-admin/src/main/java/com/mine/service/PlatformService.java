package com.mine.service;


import com.mine.bean.JsonResult;
import com.mine.bean.Platform;

import java.util.List;
import java.util.Map;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 11:19
 */
public interface PlatformService {

     Map<String,Object> list(Platform platform);

     JsonResult<List<Map<String, Object>>> listPlatformAndApp();

     Map<String,Object> add(Platform platformBean);

     Map<String,Object> delete(String id);

     Map<String,Object> update(Platform platformBean);

     /**
      * 查询给用户授权服务的树形结构
      * @param userId
      * @return
      */
     List<Map<String,Object>> queryApplicationTree(Integer userId);



}
