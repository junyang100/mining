package com.mine.service.impl;

import com.alibaba.fastjson.JSON;
import com.mine.bean.ServiceInfo;
import com.mine.config.KafkaProperties;
import com.mine.service.ConfigChangePushService;
import com.mine.service.EurekaService;
import com.mine.service.RefreshConfigService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 使用kafka发送消息的机制通知应用，配置信息已经被修改
 */


public class KafkaRefreshConfigServiceImpl implements RefreshConfigService {

    @Autowired
    private KafkaProperties kafkaProperties;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private KafkaProducer<String, String> kafkaProducer;

    @Autowired
    private EurekaService eurekaService;

    @Autowired
    private ConfigChangePushService configChangePushServcie;

    @PostConstruct
    private void initKafka() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ProducerConfig.ACKS_CONFIG, String.valueOf(kafkaProperties.getAcks()));
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(kafkaProperties.getBatchSize()));
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, String.valueOf(kafkaProperties.getBufferMemory()));
        properties.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(kafkaProperties.getRetries()));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        if (kafkaProperties.getSaslConfig() != null && kafkaProperties.getSaslConfig().trim().length() > 0) {
            properties.put("sasl.jaas.config", kafkaProperties.getSaslConfig());
            properties.put("sasl.mechanism", "PLAIN");
            properties.put("security.protocol", "SASL_PLAINTEXT");
        }
        try {
            kafkaProducer = new KafkaProducer<>(properties);
            logger.info("Kafka Producer init finished....");
            logger.info("system use kafka refersh config........");
        } catch (Exception e) {
            logger.info("init kafka error,send config change msg will not do", e);
        }

    }


    @Override
    public void refreshAll(String applicationName, String refreshDescription) {
        Map<String, List<ServiceInfo>> appNameServerInfoMap = eurekaService.queryPlatformOrCommonOrAppInfo(applicationName);
        refreshPart(applicationName, appNameServerInfoMap, refreshDescription);
    }





    @PreDestroy
    public void close(){
        if(kafkaProducer != null){
            kafkaProducer.flush();
            kafkaProducer.close();
        }
    }

    @Override
    public void refreshPart(String applicationName,Map<String, List<ServiceInfo>> appNameServerInfoMap , String refreshDescription) {
        if (appNameServerInfoMap == null || appNameServerInfoMap.size() == 0) {
            throw new RuntimeException("there is no instance in  current application[ " + applicationName + "]");
        }

        /**
         * 遍历所有的服务，发送信息到相应的主题
         */
        for (Map.Entry<String, List<ServiceInfo>> entry : appNameServerInfoMap.entrySet()) {
            String applicatonName = entry.getKey();
            Map<String, Integer> dataMap = configChangePushServcie.getPushData(entry.getValue(), refreshDescription);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(applicatonName, JSON.toJSONString(dataMap));
            kafkaProducer.send(producerRecord);
        }
    }
}
