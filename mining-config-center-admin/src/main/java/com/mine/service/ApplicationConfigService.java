package com.mine.service;


import com.mine.bean.ApplicationConfig;
import com.mine.bean.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 11:19
 */
public interface ApplicationConfigService {

     /**
      * 根据条件查询 服务配置列表
      * @param config
      * @return
      */
     JsonResult<List<ApplicationConfig>> list(ApplicationConfig config);

     /**
      * 添加服务配置
      * @param config
      * @return
      */
     JsonResult<Object> add(ApplicationConfig config);

     /**
      * 删除配置
      * @param config
      * @return
      */
     JsonResult<Object> delete(ApplicationConfig config);

     /**
      *  更新配置，并根据需要推送配置变更到所有服务
      * @param config
      * @param flag 是否刷新应用配置
      * @param ipPorts 刷新应用配置的ip端口,多个用逗号隔开，用来做灰度推送
      * @param modifyDescription 修改描述信息，用来在配置推送查询界面查询
      * @return
      */
     JsonResult<Object> update(ApplicationConfig config, String flag, String ipPorts, String modifyDescription);

     /**
      * 拷贝某个版本的配置
      * @param paramsMap
      * @return
      */
     JsonResult<Object> copyApplicationProperties(Map<String, String> paramsMap);


     /**
      * 拷贝某个版本的配置
      * @param
      * @return
      */
     JsonResult<List<ApplicationConfig>> queryApplicationUsedConfigs(ApplicationConfig config);


}
