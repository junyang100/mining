package com.mine.share;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * bootstrapcontext 无法自动注入DiscoveryClient，
 * 所以这里自己实现从Eureka获取配置中心url列表
 */
public class EurekaService {


    private RestTemplate restTemplate;


    private String[] eurekaURLArray;

    @PostConstruct
    public void init(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(5000);
        requestFactory.setConnectTimeout(2000);
        this.restTemplate  = new RestTemplate(requestFactory);
        eurekaURLArray = eurekaUrls.split(",");
        retryTimes = eurekaURLArray.length;
    }

    @Value("${eureka.client.serviceUrl.defaultZone}")
    private String eurekaUrls ;

    private AtomicInteger index = new AtomicInteger(1);

    private int retryTimes = 3;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<String> queryApplicationURLs(String serviceId) {
            List<String> urlList = new ArrayList<>();
            Throwable error = null;
            for (int i = 0; i < retryTimes; i++) {
                try {

                        HttpHeaders requestHeaders = new HttpHeaders();
                        requestHeaders.add("Accept", "application/json");
                        String url = getUrl();
                        String queryInstanceUrl = url + "apps/" + serviceId;
                        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
                        ResponseEntity<String> response = restTemplate.exchange(queryInstanceUrl, HttpMethod.GET, requestEntity, String.class);
                        String result = response.getBody();
                        if(response.getStatusCode() == HttpStatus.OK){
                            Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
                            Map<String, Object> instanceMap = (Map<String, Object>) resultMap.get("application");
                            List<Map<String, Object>> instanceList = (List<Map<String, Object>>) instanceMap.get("instance");
                            for (Map<String, Object> singleInstance : instanceList) {
                                String appAddress = singleInstance.get("ipAddr").toString();
                                Map<String, Object> normalPortMap = (Map<String, Object>) singleInstance.get("port");
                                int port = Integer.parseInt(normalPortMap.get("$").toString());
                                String hostPrefix = "http://" + appAddress + ":" + port  + "/";
                                urlList.add(hostPrefix);
                            }
                            return urlList;
                        }

                } catch (Exception e) {
                    error = e;
                }

            }

            if(urlList.size() == 0 || error != null){
                logger.error("can not query config server url from eureka",error);
            }


        return urlList;
    }


    private String getUrl(){
        int indexValue = index.getAndIncrement();
        int absoluteIndexValue = Math.abs(indexValue);
        String url = eurekaURLArray[absoluteIndexValue % eurekaURLArray.length];
        return url;
    }


   public String getRandomConfigURL(String servcieid){
            Random random = new Random();
            List<String> urls = queryApplicationURLs(servcieid);
            return urls.get(random.nextInt(urls.size()));
   }



}
