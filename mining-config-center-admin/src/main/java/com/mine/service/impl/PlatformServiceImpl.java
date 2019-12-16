package com.mine.service.impl;

import com.mine.bean.Application;
import com.mine.bean.JsonResult;
import com.mine.bean.Platform;
import com.mine.mapper.PlatformMapper;
import com.mine.service.PlatformService;
import com.mine.service.PrivilegeService;
import com.mine.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by HollyLee on 18/2/8.
 */
@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformMapper platformMapper;

    @Autowired
    private PrivilegeService privilegeService;

    @Override
    public Map<String, Object> list(Platform platform) {
        Map<String, Object> result = new HashMap<>();
        int count = platformMapper.count(platform);
        if(count > 0){
            result.put("count", count);
            List<Platform> list = platformMapper.list(platform);
            result.put("data", list);
        }
        result.put("status", "SUCCESS");
        result.put("code", 0);
        return result;
    }

    @Override
    public JsonResult<List<Map<String, Object>>> listPlatformAndApp() {

        /**  从session中获取用户的权限      **/

        Set<String> applicationSet  =  WebUtils.getUserPrivilegeFromSession();;
        if(applicationSet.size() == 0){
            return new JsonResult(JsonResult.OK_STATUS,JsonResult.OK_MSG,Collections.EMPTY_LIST,0);
        }
        List<Map<String, Object>> list = platformMapper.listPlatformAndApp(applicationSet);
        String tmp_platform_id = null;
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> value = new ArrayList<>();
        for(Map<String, Object> app : list) {
            String key = app.get("platform_name") + "(" +app.get("platform_id") + ")";
            if(StringUtils.isNotBlank(tmp_platform_id) && tmp_platform_id.equalsIgnoreCase(key)) {

            } else {
                tmp_platform_id = key;
                map = new HashMap<>();
                map.put("key", key);
                map.put("value", new ArrayList<Map<String, Object>>());
                value.add(map);
            }
            ((List)map.get("value")).add(app);
        }

        return new JsonResult(JsonResult.OK_STATUS,JsonResult.OK_MSG,value,value.size());
    }

    @Override
    public Map<String, Object> add(Platform platform) {
        Map<String, Object> result = new HashMap<String, Object>();
        platformMapper.insert(platform);
        result.put("status", "SUCCESS");
        return result;
    }

    @Override
    public Map<String, Object> delete(String id) {
        Map<String, Object> result = new HashMap<>();
        platformMapper.deleteByPrimaryKey(id);
        result.put("status", "SUCCESS");
        return result;
    }

    @Override
    public Map<String, Object> update(Platform platformBean) {
        Map<String, Object> result = new HashMap<>();
        platformMapper.updateByPrimaryKey(platformBean);
        result.put("status", "SUCCESS");
        return result;
    }

    @Override
    public List<Map<String,Object>> queryApplicationTree(Integer userId) {

        try{

            Set<String> hasPrivilegeIdSet = privilegeService.queryUserPrivilege(userId);

            List<Map<String,Object>> dataMap = new ArrayList<>();
            List<Platform> platformList = platformMapper.selectApplicationTree();
            for(Platform platform : platformList){
                Map<String,Object> tempMap = new HashMap(4);
                List<Map<String,Object>> children = new ArrayList<>();
                tempMap.put("title",platform.getPlatformName());
                tempMap.put("value",platform.getId());
                tempMap.put("data",children);
                dataMap.add(tempMap);
                List<Application> applicationList = platform.getApplicationList();
                for(Application application : applicationList){
                    tempMap = new HashMap<>(4);
                    tempMap.put("title",application.getApplicationName());
                    tempMap.put("value",application.getId());
                    tempMap.put("data",Collections.EMPTY_LIST);
                    if(hasPrivilegeIdSet.contains(application.getId())){
                        tempMap.put("checked",true);
                    }
                    children.add(tempMap);
                }
            }
            return  dataMap;
        }catch(Exception e){
            throw new RuntimeException("system error",e);
        }
    }



}
