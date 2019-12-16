package com.mine.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.mine.share.PushRefreshResultService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;

import javax.annotation.PreDestroy;
import java.util.Map;

public class ZKRefresher {

    private ContextRefresher contextRefresher;

    private CuratorFramework client;

    private String applicationName;

    @Value("${spring.cloud.client.ipAddress}")
    private String ip;

    @Value("${server.port}")
    private String port;

    @Autowired
    private PushRefreshResultService pushRefreshResultService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ZKRefresher(ContextRefresher contextRefresher, CuratorFramework client, String applicationName) {
        this.contextRefresher = contextRefresher;
        this.client = client;
        this.applicationName = applicationName;
    }


    public void start() {
        if(client == null){
            return;
        }
        String applicationWatchPath = "/"+applicationName;
        try {
            if(client.checkExists().forPath(applicationWatchPath) == null){
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(applicationWatchPath);
            }

        } catch (Exception e) {
            logger.warn("create node error",e);
        }

        try{
            NodeCache nodeCacheApplication = new NodeCache(client,applicationWatchPath,false);
            nodeCacheApplication.start(true);
            nodeCacheApplication.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    String data = new String(client.getData().forPath(applicationWatchPath),"utf-8");
                    refershConfig(data);
                }
            });
            logger.info("zk refersh config start successfully.....");
        }catch (Exception e){
            //ignore
        }

    }


    public void refershConfig(String data){
        Object id = null;
        try{
            Map<String,Object> dataMap = (Map<String, Object>) JSON.parseObject(data,Map.class);
            String ipPort = ip + ":" + port;
            id = dataMap.get(ipPort);
            if(id != null){
                long beginTime = System.currentTimeMillis();
                logger.info("recieve zk notice,begin to refresh application [{}]",applicationName);
                contextRefresher.refresh();
                pushRefreshResultService.pushRefreshResult("2",id.toString());
                logger.info("refresh [{}] finished,cost time [{}] mills",applicationName,(System.currentTimeMillis() - beginTime));
            }
        }catch (Exception e){
            pushRefreshResultService.pushRefreshResult("3",id.toString());
            logger.error("refersh config error",e);
        }
    }




    @PreDestroy
    public void close(){
        if(client != null){
            client.close();
        }

    }



}
