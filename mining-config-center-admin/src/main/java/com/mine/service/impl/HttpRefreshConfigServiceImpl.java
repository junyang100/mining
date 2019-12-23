package com.mine.service.impl;
import com.mine.bean.ServiceInfo;
import com.mine.service.ConfigChangePushService;
import com.mine.service.EurekaService;
import com.mine.service.RefreshConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * 从eureka 获取所有服务的实例，
 * 依次调用所有服务的刷新方法
 */
public class HttpRefreshConfigServiceImpl implements RefreshConfigService {

    @Autowired
    private EurekaService eurekaService;

    @Autowired
    private ConfigChangePushService configChangePushService;

    @Autowired
    private ExecutorService executorService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;

    public HttpRefreshConfigServiceImpl(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);
        httpRequestFactory.setReadTimeout(3000);
        restTemplate = new RestTemplate(httpRequestFactory);
        logger.info("system use http refersh config........");
    }


    @Override
    public void refreshAll(String appName,String refreshDescription) {
            Map<String, List<ServiceInfo>> appNameServerInfoMap = eurekaService.queryPlatformOrCommonOrAppInfo(appName);
            refreshPart(appName,appNameServerInfoMap,refreshDescription);
    }



    @Override
    public void refreshPart(String applicationName,Map<String, List<ServiceInfo>> appNameServerInfoMap, String refreshDescription) {

        if(appNameServerInfoMap == null ||appNameServerInfoMap.size() == 0){
            throw new RuntimeException("there is no instance in  current application[ "+applicationName+ "]");
        }

        List<ServiceInfo> allServiceInfoList = new ArrayList<>();
        for(List<ServiceInfo> serviceInfos :appNameServerInfoMap.values()){
            allServiceInfoList.addAll(serviceInfos);
        }

        Map<String,Integer> dataMap =  configChangePushService.getPushData(allServiceInfoList,refreshDescription);
        for(ServiceInfo serviceInfo : allServiceInfoList ){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    MultiValueMap<String, String> map= new LinkedMultiValueMap<>(4);
                    map.add("id",dataMap.get(serviceInfo.getIp() + ":" +serviceInfo.getPort()).toString());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
                    try{
                        restTemplate.postForEntity( serviceInfo.getActuatorURI()+ "refresh/refreshConfig",request,String.class);
                    }catch (Exception e){
                        logger.error("refresh app error",e);
                    }
                }
            });
        }
    }
}
