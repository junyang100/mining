package com.mine.service.impl;

import com.mine.bean.Application;
import com.mine.bean.ServiceInfo;
import com.mine.service.ApplicationService;
import com.mine.service.EurekaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class EurekaServiceImpl implements EurekaService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public Map<String,List<ServiceInfo>> queryPlatformOrCommonOrAppInfo(String applicationName) {
        Map<String,List<ServiceInfo>> serviceInfoHashMap = new HashMap<>(16);
        List<Application> applications;
        /**   common 所有应用的配置，获取所有的应用           **/
        if("common".equals(applicationName)){
            applications = applicationService.queryPlatformApplication(null);
        }else if(applicationService.isPlatformConfig(applicationName)){
            applications = applicationService.queryPlatformApplication(applicationName);
        }else{
            applications = new ArrayList<>(10);
            Application application = new Application();
            application.setId(applicationName);
            applications.add(application);
        }
        for(Application application : applications){
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(application.getId());
            List<ServiceInfo> serviceInfos = new ArrayList<>(serviceInstances.size());
            for(ServiceInstance serviceInstance : serviceInstances){
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setApplicationName(application.getId());
                serviceInfo.setIp(serviceInstance.getHost());
                serviceInfo.setPort(serviceInstance.getPort());
                setURL(serviceInstance,serviceInfo);
                serviceInfos.add(serviceInfo);
            }
            serviceInfoHashMap.put(application.getId(),serviceInfos);
        }
        return serviceInfoHashMap;
    }


    private void setURL(ServiceInstance serviceInstance, ServiceInfo serviceInfo){
        String ip = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        String schema = "http";
        if(serviceInstance.isSecure()){
            schema = "https";
        }
        StringBuilder stringBuilder = new StringBuilder(16);
        stringBuilder.append(schema + "://").append(ip).append(":").append(port);
        Map<String,String> metaData = serviceInstance.getMetadata();
        String contextPath = metaData.get("contextPath");
        if(contextPath != null){
            stringBuilder.append(contextPath).append("/");
        }else{
            stringBuilder.append("/");
        }
        serviceInfo.setUri(stringBuilder.toString());
        StringBuilder managementStringBuilder = new StringBuilder(16);
        String managementPortStr = metaData.get("managementPort");
        String managementContextPath = metaData.get("managementContextPath");
        managementStringBuilder.append(schema + "://").append(ip).append(":");
        if(managementPortStr != null){
            managementStringBuilder.append(managementPortStr);
            if(managementContextPath != null){
                managementStringBuilder.append(managementContextPath).append("/");
            }else{
                managementStringBuilder.append("/");
            }
        }else{
            managementStringBuilder.append(port);
            if(managementContextPath != null){
                managementStringBuilder.append(managementContextPath).append("/");
            }else{
                if(contextPath != null){
                    managementStringBuilder.append(contextPath).append("/");
                }else{
                    managementStringBuilder.append("/");
                }
            }
        }
        serviceInfo.setActuatorURI(managementStringBuilder.toString());
    }
}
