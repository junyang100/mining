package com.mine.share;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过http方式把刷新结果告知配置中心管理端
 */
public class PushRefreshResultService {

     @Autowired
     private DiscoveryClient discoveryClient;

     private String pushServiceName = "config-center";

     private Logger logger = LoggerFactory.getLogger(this.getClass());

     private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 2);

     private RestTemplate restTemplate = new RestTemplate();

     private AtomicInteger index = new AtomicInteger(0);

     @PostConstruct
     public void buildRestTemplate(){
         HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
         httpRequestFactory.setConnectionRequestTimeout(3000);
         httpRequestFactory.setConnectTimeout(3000);
         httpRequestFactory.setReadTimeout(3000);
         restTemplate = new RestTemplate(httpRequestFactory);
     }

    /**
     * @param status 刷新状态
     * @param id 主键
     */
     public void pushRefreshResult(String status,String id){
             executorService.submit(new Runnable() {
                 @Override
                 public void run() {
                     MultiValueMap<String, String> map= new LinkedMultiValueMap<>(4);
                     map.add("id",id);
                     map.add("status",status);
                     HttpHeaders headers = new HttpHeaders();
                     headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                     HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
                     for(int i = 0;i < 3;i++){
                         try{
                             //String url = "http://localhost:8001/pushConfigResult/modifyConfigPushResult";
                             String url = getURL();
                             String result = restTemplate.postForEntity( url,request,String.class).getBody();
                             Map<String,Object> mapResult = JSON.parseObject(result,Map.class);
                             if(mapResult.get("status").toString().equals("1")){
                                 logger.info("push config success");
                                 return;
                             }
                         }catch (Exception e){
                             logger.error("push config result error",e);
                         }
                     }
                 }
             });
     }

     private String getURL(){
         List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(pushServiceName);
         int serviceIndex = Math.abs(index.getAndIncrement()) % serviceInstanceList.size();
         return  serviceInstanceList.get(serviceIndex).getUri().toString() + "pushConfigResult/modifyConfigPushResult";
     }

}
