package com.mine.service.impl;

import com.alibaba.fastjson.JSON;
import com.mine.bean.ServiceInfo;
import com.mine.config.ZookeeperProperties;
import com.mine.service.ConfigChangePushService;
import com.mine.service.EurekaService;
import com.mine.service.RefreshConfigService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;


public class ZKRefreshConfigServiceImpl implements RefreshConfigService {

    private CuratorFramework client;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EurekaService eurekaService;

    @Autowired
    private ConfigChangePushService configChangePushServcie;

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @PostConstruct
    public void init(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(zookeeperProperties.getConnectString())
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
                .namespace(zookeeperProperties.getNamespace())
                .retryPolicy(new ExponentialBackoffRetry(1000,zookeeperProperties.getMaxRetryCount()))
                .build();
          curatorFramework.start();
          client = curatorFramework;
          logger.info("system use zk refersh config........");
    }


    @Override
    public void refreshAll(String applicationName,String refreshDescription) {
        Map<String, List<ServiceInfo>> appNameServerInfoMap = eurekaService.queryPlatformOrCommonOrAppInfo(applicationName);
        refreshPart(applicationName,appNameServerInfoMap,refreshDescription);
    }

    @Override
    public void refreshPart(String applicationName, Map<String, List<ServiceInfo>> appNameServerInfoMap, String refreshDescription) {
        if(appNameServerInfoMap == null ||appNameServerInfoMap.size() == 0){
            throw new RuntimeException("there is no instance in  current application[ "+applicationName+ "]");
        }
        /**
         * 遍历所有的服务，写入信息到zk,
         */
        for(Map.Entry<String,List<ServiceInfo>> entry : appNameServerInfoMap.entrySet()){
            String appName = entry.getKey();
            String path = "/" + appName;
            Map<String, Integer> dataMap = configChangePushServcie.getPushData(entry.getValue(), refreshDescription);
            try {
                if(client.checkExists().forPath(path) == null){
                    client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                }
            } catch (Exception e) {
                logger.warn("create node error",e);

            }
            try {
                client.setData().forPath(path, JSON.toJSONString(dataMap).getBytes("utf-8"));
            } catch (Exception e) {
                logger.error("refersh cofig error",e);
            }

        }
    }


    @PreDestroy
    public void destory(){
        if(client != null){
            client.close();
        }
    }

}






