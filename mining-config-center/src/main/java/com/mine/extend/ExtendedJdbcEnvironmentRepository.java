package com.mine.extend;


import com.mine.bean.ApplicationConfig;
import com.mine.mapper.ApplicationConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * zyp
 * 从关系数据库获取系统配置信息
 */

public class ExtendedJdbcEnvironmentRepository implements EnvironmentRepository{




    @Autowired
    private ApplicationConfigMapper applicationConfigMapper;

    public ExtendedJdbcEnvironmentRepository(){

    }



    @Override
    public Environment findOne(String application, String profile, String label) {

        if(StringUtils.isEmpty(application) || StringUtils.isEmpty(profile) ||
                StringUtils.isEmpty(label)){
            throw new IllegalArgumentException("application,profile,label must not by empty");
        }
        /**   根据服务名查询所属平台，       **/
        String platformName =  applicationConfigMapper.selectAppPlatform(application);
        /** 查询特定环境特定配置   **/
        ApplicationConfig paramConfig = new ApplicationConfig();
        paramConfig.setApplication(application);
        paramConfig.setLabel(label);
        paramConfig.setProfile(profile);
        paramConfig.setPlatform(platformName);
        List<Map<String,Object>> mapConfigList = applicationConfigMapper.selectAppConfig(paramConfig);

        /**
         * 把所有配置加入到 一个map中，加入顺序为 通用的，默认的，和特定环境的，
         * 如果有key相同，则默认使用 特定环境的，
         * 这里用了map集合的相同的key，替换值，
         */
        Map<String,Object> configMap = new LinkedHashMap<>();
        for(Map<String,Object> map : mapConfigList){
                configMap.put(map.get("KEY").toString(),map.get("VALUE"));
        }

        Environment environment = new Environment(application, new String[]{profile}, label, null,
                null);
        if(!configMap.isEmpty()){
            environment.add(new PropertySource(application + "-" + profile ,configMap));
        }
        return environment;


    }





}
