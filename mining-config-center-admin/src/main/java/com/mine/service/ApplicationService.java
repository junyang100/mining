package com.mine.service;


import com.mine.bean.Application;

import java.util.List;
import java.util.Map;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 11:19
 */
public interface ApplicationService {

     Map<String,Object> list(Application application);

     Map<String,Object> add(Application applicationBean);

     Map<String,Object> delete(String id);

     Map<String,Object> update(Application applicationBean);

     /**
      * 查询某个平台的下的所有的服务,如果平台为空，则查询所有的服务
      * @param platform
      * @return
      */
     List<Application> queryPlatformApplication(String platform);

     /**
      * 判断一个应用是否是平台配置,
      * 判断方法平台编码和服务编码相同
      * @param applicationName
      * @return
      */
     boolean isPlatformConfig(String applicationName);


}
