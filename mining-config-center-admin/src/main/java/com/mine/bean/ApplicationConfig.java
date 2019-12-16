package com.mine.bean;

import java.util.Date;
import java.util.Set;

/**
 * 应用系统配置
 * zyp
 */
public class ApplicationConfig extends Base{

    /**  主键    **/
    private Integer id;

    /**  应用名称    **/
    private String application;

    /**  环境    **/
    private String profile;

    /**  环境描述    **/
    private String profileDesc;

    /**  版本号    **/
    private String label;

    /**  属性名称    **/
    private String key;

    /**  属性值    **/
    private String value;

    /**  属性描述    **/
    private String description;

    /**  主键    **/
    private Date createTime;

    /**  创建时间    **/
    private Date updateTime;

    /**  配置组    **/
    private String configGroup;

    /**  版本描述    **/
    private String labelDesc;

    /**  配置所属平台    **/
    private String platform;

    /**
     * 用户能够查看的服务
     */
    private Set<String> applications;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application == null ? null : application.trim();
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile == null ? null : profile.trim();
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc == null ? null : profileDesc.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup == null ? null : configGroup.trim();
    }

    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc == null ? null : labelDesc.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public Set<String> getApplications() {
        return applications;
    }

    public void setApplications(Set<String> applications) {
        this.applications = applications;
    }


}