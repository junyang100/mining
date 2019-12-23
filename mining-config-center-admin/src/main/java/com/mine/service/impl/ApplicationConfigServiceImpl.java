package com.mine.service.impl;
import com.mine.annotation.AppPrivilege;
import com.mine.bean.ApplicationConfig;
import com.mine.bean.JsonResult;
import com.mine.mapper.ApplicationConfigMapper;
import com.mine.service.ApplicationConfigService;
import com.mine.service.RefreshConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author guoyangyang
 * Date: 2018/1/4
 * Time: 11:22
 */
@Service
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RefreshConfigService refreshConfigService;

    @Autowired
    private ApplicationConfigMapper applicationConfigMapper;

    @Override
    @Deprecated
    public JsonResult<Object> copyApplicationProperties(Map<String, String> paramsMap) {
         int status = 200;
         String msg = "";
         String application = paramsMap.get("application");
         String oldVersion = paramsMap.get("oldVersion");
         String newVersion = paramsMap.get("newVersion");
         String profile = paramsMap.get("profile");
         if(StringUtils.isEmpty(application)){
             status = JsonResult.PARAM_ERROR;
             msg = "application must not empty";

         }else if(StringUtils.isEmpty(oldVersion)){
             status = JsonResult.PARAM_ERROR;
             msg = "oldVersion must not empty";
         }else if(StringUtils.isEmpty(newVersion)){
             status = JsonResult.PARAM_ERROR;
             msg = "newVersion must not empty";
         }else if(StringUtils.isEmpty(profile)){
             status = JsonResult.PARAM_ERROR;
             msg = "profile must not empty";
         }
         int duplicateRows = 0;
         try{
             if(status != 1) {
                 duplicateRows = applicationConfigMapper.duplicate(new Date(),newVersion,profile,application,oldVersion);
                 msg = "success";
             }

         }catch(Exception e){
            logger.error("copy application properties error",e);
            msg = "copy application properties error";
            status = 1;
         }
         return new JsonResult<>(status,msg,null,duplicateRows);
    }


    @AppPrivilege
    @Override
    public JsonResult<List<ApplicationConfig>> list(ApplicationConfig config) {
        int count = applicationConfigMapper.count(config);
        List<ApplicationConfig> list = null;
        if(count > 0){
            list = applicationConfigMapper.list(config);
        }
        return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,list,count);
    }

    @AppPrivilege
    @Override
    public JsonResult<Object> add(ApplicationConfig config) {
       int count =  applicationConfigMapper.add(config);
       return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,count);
    }

    @AppPrivilege
    @Override
    public JsonResult<Object> delete(ApplicationConfig applicationConfig) {
        int deleteCount =  applicationConfigMapper.delete(applicationConfig);
        if(deleteCount == 1){
            return new JsonResult<>(JsonResult.OK_STATUS,"删除成功!",null,deleteCount);
        }else {
            return new JsonResult<>(JsonResult.BUSINESS_ERROR,"删除失败!,可能权限不足!",null,deleteCount);
        }

    }

    @AppPrivilege
    @Override
    public JsonResult<Object> update(ApplicationConfig config,String flag,String refreshJson,String modifyDescription) {
        int updateCount = applicationConfigMapper.update(config);
        if("on".equals(flag)){
            /**
             * 刷新当前应用的配置
             */
            if(StringUtils.isNotEmpty(refreshJson)){
                //{"app1":[{"ip":"33333",port:2233},{"ip":"33333",port:2233}]}
                refreshConfigService.refreshPart(config.getApplication(),null,modifyDescription);
            }else{
                refreshConfigService.refreshAll(config.getApplication(),modifyDescription);
            }

        }
        return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,null,updateCount);
    }

    @AppPrivilege
    @Override
    public JsonResult<List<ApplicationConfig>> queryApplicationUsedConfigs(ApplicationConfig config) {

        String platform = applicationConfigMapper.selectAppPlatform(config.getApplication());
        config.setPlatform(platform);
        List<ApplicationConfig> applicationConfigs = applicationConfigMapper.selectAppUsedConfig(config);
        Map<String,ApplicationConfig> applicationConfigMap = new LinkedHashMap(applicationConfigs.size());
        /**
         * 去除重复的key
         */
        for(ApplicationConfig applicationConfig :applicationConfigs){
            applicationConfigMap.put(applicationConfig.getKey(),applicationConfig);
        }
        /**     按照服务名排序       **/
        List<ApplicationConfig> nameSortList = new ArrayList<>(applicationConfigMap.values());
        Collections.sort(nameSortList, new Comparator<ApplicationConfig>() {
            @Override
            public int compare(ApplicationConfig o1, ApplicationConfig o2) {
                return o1.getApplication().compareTo(o2.getApplication());
            }
        });
        return new JsonResult<>(JsonResult.OK_STATUS,JsonResult.OK_MSG,nameSortList);

    }
}
