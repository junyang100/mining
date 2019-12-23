package com.mine.service.impl;

import com.mine.bean.Application;
import com.mine.mapper.ApplicationMapper;
import com.mine.mapper.UserPrivillegeMapper;
import com.mine.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HollyLee on 18/2/8.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserPrivillegeMapper userPrivillegeMapper;

    @Override
    public Map<String, Object> list(Application application) {
        Map<String, Object> result = new HashMap<>();

        int count = applicationMapper.count(application);
        if(count>0){
            result.put("count", count);
            List<Application> list = applicationMapper.list(application);
            result.put("data", list);
        }
        result.put("status", "SUCCESS");
        result.put("code", 0);
        return result;
    }

    @Override
    public Map<String, Object> add(Application applicationBean) {
        Map<String, Object> result = new HashMap<String, Object>();
        applicationMapper.insert(applicationBean);
        result.put("status", "SUCCESS");
        return result;
    }

    @Transactional
    @Override
    public Map<String, Object> delete(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        applicationMapper.deleteByPrimaryKey(id);
        userPrivillegeMapper.deleteByPrivilegeId(id);
        result.put("status", "SUCCESS");
        return result;
    }

    @Override
    public Map<String, Object> update(Application applicationBean) {
        Map<String, Object> result = new HashMap<String, Object>();
        applicationMapper.updateByPrimaryKey(applicationBean);
        result.put("status", "SUCCESS");
        return result;
    }

    @Override
    public List<Application> queryPlatformApplication(String platform) {
        Application application = new Application();
        application.setPlatform(platform);
        return applicationMapper.selectApplicationByPlatform(application);
    }

    @Override
    public boolean isPlatformConfig(String applicationName) {
        return applicationMapper.selectApplicationIsPlatformCount(applicationName) > 0;
    }
}
