package com.mine.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 配置推送记录
 */
public class ConfigChangePush extends Base{

    private Long id;

    private String application;

    private String applicationIp;

    private Integer applicationPort;

    private Byte pushStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date pushTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date pushFinishTime;

    private String pushUserName;

    private String pushDescription;

    private String beginTimeStr;

    private String endTimeStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application == null ? null : application.trim();
    }

    public String getApplicationIp() {
        return applicationIp;
    }

    public void setApplicationIp(String applicationIp) {
        this.applicationIp = applicationIp == null ? null : applicationIp.trim();
    }

    public Integer getApplicationPort() {
        return applicationPort;
    }

    public void setApplicationPort(Integer applicationPort) {
        this.applicationPort = applicationPort;
    }

    public Byte getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Byte pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Date getPushFinishTime() {
        return pushFinishTime;
    }

    public void setPushFinishTime(Date pushFinishTime) {
        this.pushFinishTime = pushFinishTime;
    }

    public String getPushUserName() {
        return pushUserName;
    }

    public void setPushUserName(String pushUserName) {
        this.pushUserName = pushUserName == null ? null : pushUserName.trim();
    }


    public String getPushDescription() {
        return pushDescription;
    }

    public void setPushDescription(String pushDescription) {
        this.pushDescription = pushDescription;
    }

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr) {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }
}