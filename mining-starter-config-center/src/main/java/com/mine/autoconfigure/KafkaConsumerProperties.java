package com.mine.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.refersh.kafka")
public class KafkaConsumerProperties {

    private String bootstrapServers;

    private boolean autocommit = true;

    private String groupId;

    private String autoOffsetReset = "latest";

    private String topic;

    private boolean enable = true;

    private String saslConfig;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public boolean isAutocommit() {
        return autocommit;
    }

    public void setAutocommit(boolean autocommit) {
        this.autocommit = autocommit;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSaslConfig() {
        return saslConfig;
    }

    public void setSaslConfig(String saslConfig) {
        this.saslConfig = saslConfig;
    }
}
