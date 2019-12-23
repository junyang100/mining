package com.mine.share;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 *增加带本地缓存的属性资源定位器
 */
public class LocalCachePropertySourceLocator implements PropertySourceLocator {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigClientProperties configClientProperties;

    @Autowired
    private RetryProperties retryProperties;

    private RestTemplate restTemplate;

    private File localCacheFile;


    @Autowired
    private ConfigServiceURLProvider configServiceURLProvider;

    private static final String FILE_ENCODING= "UTF-8";

    private static final String CONFIG_NAME = "config.json";


    public LocalCachePropertySourceLocator() {

    }

    @PostConstruct
    public void init(){
        restTemplate = getRestTemplate();
        if(configClientProperties.isLocalCacheEnbaled()){
            File localFileDir =  new File(System.getProperty("user.dir") + "/config-cache");
            if(!localFileDir.exists()){
                localFileDir.mkdirs();
            }
            localCacheFile = new File( localFileDir, CONFIG_NAME);
            Timer timer = new Timer("config-local-file");
            timer.schedule(new StoreRemoteConfigFileTask(),120 * 1000,configClientProperties.getLocalConfigCacheUpdateInterval());
            logger.info("local cache thread started successfully.......");
        }

    }


    @Override
    public PropertySource<?> locate(Environment environment) {
        CompositePropertySource composite = new CompositePropertySource("localCacheConfigServcie");
        Exception error = null;
        for(int i = 0;i <retryProperties.getMaxAttempts();i++) {
            logger.info("Fetching config from server at: {}",configServiceURLProvider.getConfigServerURL(configClientProperties.getDiscovery().getServiceId()));
            try {
                com.mine.environment.Environment result = getRemoteEnvironment();
                if (result != null) {
                    logger.info("Located environment: name={}, profiles={}, label={}",
                            result.getName(),
                            result.getProfiles() == null ? "" : Arrays.asList(result.getProfiles()),
                            result.getLabel());
                    addFoundedPropertySource(composite, result);
                    return composite;
                }

            }  catch (Exception e) {
                error = e;
                if(!configClientProperties.isFailFast()){
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(retryProperties.initialInterval);
                } catch (InterruptedException e1) {
                    //ignore
                }
            }
        }
        if(configClientProperties.isLocalCacheEnbaled()){
            logger.warn("fetch config error,try using local cache config");
            com.mine.environment.Environment localEnvironment = getLocalEnvironment();
            if(localEnvironment != null){
                addFoundedPropertySource(composite,localEnvironment);
                logger.info("fetch config from local file successfully.......");
                return composite;
            }
        }

        throw new IllegalStateException(
                    "Could not locate PropertySource form Remote and local, failing",
                    error);
    }

    private void addFoundedPropertySource(CompositePropertySource composite, com.mine.environment.Environment result) {
        if (result.getPropertySources() != null) { // result.getPropertySources() can be null if using xml
            for (com.mine.environment.PropertySource source : result.getPropertySources()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) source
                        .getSource();
                composite.addPropertySource(new MapPropertySource(source
                        .getName(), map));
            }
        }
    }

    /**
     * 获取http客户端工具类
     * @return
     */
    private RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout((60 * 1000 * 3) + 5000); //TODO 3m5s, make configurable?
        requestFactory.setConnectTimeout(2000);
        RestTemplate template = new RestTemplate(requestFactory);
        return template;
    }

    /**
     * 从配置中心获取配置，以json格式返回
     *
     * @return
     */
    public com.mine.environment.Environment getRemoteEnvironment() {
        String path = "{name}/{profile}/{label}";
        String name = configClientProperties.getName();
        String profile = configClientProperties.getProfile();
        String label = configClientProperties.getLabel();
        String uri = configServiceURLProvider.getConfigServerURL(configClientProperties.getDiscovery().getServiceId());
        Object[] args = new String[] { name,profile,label };
        ResponseEntity<com.mine.environment.Environment> response = null;

        try {
            final HttpEntity<Void> entity = new HttpEntity<>((Void) null, null);
            response = restTemplate.exchange(uri + path, HttpMethod.GET,
                    entity, com.mine.environment.Environment.class, args);
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        com.mine.environment.Environment result = response.getBody();
        return result;
    }

    /**
     * 从本地文件读取配置
     * @return
     */
    public com.mine.environment.Environment getLocalEnvironment(){
        BufferedReader bufferedReader = null;
        try{
            if(!localCacheFile.exists()){
                return null;
            }
            InputStreamReader reader = new InputStreamReader(new FileInputStream(localCacheFile),FILE_ENCODING);
            bufferedReader = new BufferedReader(reader);
            String configJson = bufferedReader.readLine();
            return converJsonToEnv(configJson);
        }catch (Exception e){
            logger.error("read local config file error",e);
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                   //ignore only logger
                    logger.error("close reader error");
                }
            }
        }

        return null;
    }



    private com.mine.environment.Environment converJsonToEnv(String configJson){

        if(configJson != null){
            /**
             * enviroment 类没有空的构造方法，所以不能直接转换
             */
            Map<String,Object> jsonMap = JSON.parseObject(configJson,Map.class);
            String application = (String)jsonMap.get("name");
            String label = (String)jsonMap.get("label");
            String[] profiles = ((JSONArray)jsonMap.get("profiles")).toArray(new String[1]);
            Map<?,?> mapSource = (Map<?,?>)((Map<?,?>)((JSONArray)jsonMap.get("propertySources")).get(0)).get("source");
            com.mine.environment.Environment environment = new com.mine.environment.Environment(application,profiles,label, null,
                    null);

            environment.add(new com.mine.environment.PropertySource(application + "-" + profiles[0] ,mapSource));
            return environment;

        }

        return null;
    }


    /**
     * 定时持久远程配置到本地文件
     */
    class StoreRemoteConfigFileTask extends TimerTask{
        @Override
        public void run() {
            BufferedWriter bufferedWriter = null;
            try{
                String json = null;
                com.mine.environment.Environment environment = getRemoteEnvironment();
                if(environment != null){
                    json = JSON.toJSONString(environment);
                }
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(localCacheFile),FILE_ENCODING);
                bufferedWriter =  new BufferedWriter(writer);
                if(json != null){
                    bufferedWriter.write(json);
                }
            }catch (IOException e){
                logger.error("write config file to local file error",e);
            }catch (Exception e){
                logger.error("write config file to local file error",e);
            }finally {
                if(bufferedWriter != null){
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        //ignore only logger
                        logger.error("close writer error");
                    }
                }
            }

        }
    }






}



