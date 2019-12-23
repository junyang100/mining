package com.mine.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.mine.share.PushRefreshResultService;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class KafkaRefresher implements Runnable{

   private ContextRefresher refresher;

   private Logger logger = LoggerFactory.getLogger(this.getClass());

   private KafkaConsumer<String,String> kafkaConsumer;

   private KafkaConsumerProperties kafkaConsumerProperties;

   @Autowired
   private PushRefreshResultService pushRefreshResultService;


    @Value("${spring.cloud.client.ipAddress}")
    private String ip;

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String applicationName;


   private volatile  boolean closed = false;

    public KafkaRefresher(ContextRefresher refresher, KafkaConsumerProperties kafkaConsumerProperties) {
        this.refresher = refresher;
        this.kafkaConsumerProperties = kafkaConsumerProperties;
    }


    public void run() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConsumerProperties.getBootstrapServers());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,kafkaConsumerProperties.getAutoOffsetReset());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,kafkaConsumerProperties.getGroupId());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,kafkaConsumerProperties.isAutocommit());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        if(kafkaConsumerProperties.getSaslConfig() != null && kafkaConsumerProperties.getSaslConfig().trim().length() >0){
            properties.put("sasl.jaas.config",kafkaConsumerProperties.getSaslConfig());
            properties.put("sasl.mechanism", "PLAIN");
            properties.put("security.protocol", "SASL_PLAINTEXT");
        }
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(kafkaConsumerProperties.getTopic()), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                /**  每次从最后的位置开始消费       **/
                kafkaConsumer.seekToEnd(partitions);
            }
        });
        logger.info("refresh kafka consumer init ok.......");

        try{
            while(!closed){
                ConsumerRecords<String,String> consumerRecords = kafkaConsumer.poll(Integer.MAX_VALUE);
                for(ConsumerRecord<String,String> consumerRecord : consumerRecords){
                    long beginTime = System.currentTimeMillis();
                    long msgTime = consumerRecord.timestamp();
                    if(beginTime - msgTime <= 120000){
                        try{
                            Map<String,Object> dataMap = (Map<String, Object>) JSON.parseObject(consumerRecord.value(),Map.class);
                            String ipPort = ip + ":" + port;
                            Object id = dataMap.get(ipPort);
                            if(id != null){
                                logger.info("recieve kafka notice,begin to refresh application [{}]",applicationName);
                                try{
                                    refresher.refresh();
                                    logger.info("refresh [{}] finished,cost time [{}] mills",applicationName,(System.currentTimeMillis() - beginTime));
                                    pushRefreshResultService.pushRefreshResult("2",id.toString());
                                }catch (Exception e){
                                    logger.error("refersh config error",e);
                                    pushRefreshResultService.pushRefreshResult("3",id.toString());
                                }
                            }
                        }catch(Exception e){
                            logger.error("error ...",e);
                        }
                    }else{
                        logger.warn("discard three minutes ago message");
                    }
                }
            }
        }catch (WakeupException e){
            if(!closed){
                logger.error("consumer not closed,but is interrupted",e);
            }
        }
        finally {
            kafkaConsumer.close();
        }

    }


    public void close(){
        closed = true;
        kafkaConsumer.wakeup();
    }



}
