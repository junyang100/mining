package com.mine.share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 用来获取配置服务的url
 */
public class ConfigServiceURLProvider {

    @Autowired
    private ConfigClientProperties clientProperties;

    @Autowired
    private RetryProperties retryProperties;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private EurekaService eurekaService;

    public ConfigServiceURLProvider(){

    }




    public String getConfigServerURL(String serviceId) {
        if(clientProperties.getDiscovery().isEnabled()){
            logger.debug("Locating configserver (" + serviceId + ") via discovery");
             String url = null;
            for(int i = 0;i < retryProperties.maxAttempts;i++){
                try{
                     url = eurekaService.getRandomConfigURL(serviceId);
                    if(url != null){
                        break;
                    }else{
                        try {
                            TimeUnit.MILLISECONDS.sleep(retryProperties.initialInterval);
                        } catch (InterruptedException e1) {
                            //ignore
                        }
                    }
                }catch (Exception e){
                    if(!clientProperties.isFailFast()){
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(retryProperties.initialInterval);
                    } catch (InterruptedException e1) {
                        //ignore
                    }

                }

            }

            if (url == null ) {
                logger.warn("No instances found of configserver ( {} )",serviceId);
                return clientProperties.getUri();
            }

            logger.debug("Located config server ( {} ) via discovery: {}" ,serviceId, url);
            return url;

        }else{
            return clientProperties.getUri();
        }

    }



}
