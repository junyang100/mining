package com.mine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * kafka 属性配置类
 */

@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {


  private String  bootstrapServers;

  private int acks = 0;

  private int retries = 0;

  private int batchSize = 16384;

  private int bufferMemory = 32 * 1024 * 1024;

   private String saslConfig;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getAcks() {
        return acks;
    }

    public void setAcks(int acks) {
        this.acks = acks;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getSaslConfig() {
        return saslConfig;
    }

    public void setSaslConfig(String saslConfig) {
        this.saslConfig = saslConfig;
    }
}
