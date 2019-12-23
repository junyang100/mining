package com.mine.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.reflect.Field;

@ConfigurationProperties(prefix = "config.refersh.zk")
public class ZKProperties {

    private String connectString = "localhost:2181";

    private int  sessionTimeoutMs = 30000;

    private int connectionTimeoutMs = 5000;

    private String namespace = "config-refresh-node";

    private int maxRetryCount = 3;


    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getSimpleName());
        b.append(" values: ");
        b.append(System.getProperty("line.separator"));
        Field[] allFields = this.getClass().getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);
            b.append('\t');
            b.append(field.getName());
            b.append(" = ");
            try {
                b.append(field.get(this));
            } catch (Exception e) {
            }
            b.append(System.getProperty("line.separator"));
        }
        return b.toString();
    }

}
