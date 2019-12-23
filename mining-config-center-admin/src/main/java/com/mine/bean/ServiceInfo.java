package com.mine.bean;

public class ServiceInfo {

    private String ip;

    private int port;

    private String uri;

    private String applicationName;

    /**   监控的url，避免访问权限的问题     **/
    private String actuatorURI;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }


    public String getActuatorURI() {
        return actuatorURI;
    }

    public void setActuatorURI(String actuatorURI) {
        this.actuatorURI = actuatorURI;
    }
}
