package com.mine.share;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = ConfigClientProperties.PREFIX)
public class ConfigClientProperties {

    public static final String PREFIX = "spring.cloud.config";

    /**
     * Flag to say that remote configuration is enabled. Default true;
     */
    private boolean enabled = true;

    /**
     * The default profile to use when fetching remote configuration (comma-separated).
     * Default is "default".
     */
    private String profile = "default";

    /**
     * Name of application used to fetch remote properties.
     */
    @Value("${spring.application.name:application}")
    private String name;

    /**
     * The label name to use to pull remote configuration properties. The default is set
     * on the server (generally "master" for a git based server).
     */
    private String label;

    /**
     * The username to use (HTTP Basic) when contacting the remote server.
     */
    private String username;

    /**
     * The password to use (HTTP Basic) when contacting the remote server.
     */
    private String password;

    /**
     * The URI of the remote server (default http://localhost:8888).
     */
    private String uri = "http://localhost:8888";

    /**
     * Discovery properties.
     */
    private Discovery discovery = new Discovery();

    /**
     * Flag to indicate that failure to connect to the server is fatal (default false).
     */
    private boolean failFast = false;

    /**
     * 是否启用本地缓存
     */
    private boolean localCacheEnbaled = true;

    /**
     * 本地缓存更新频率 单位毫秒
     */
    private long localConfigCacheUpdateInterval = 600000;


    private ConfigClientProperties() {
    }



    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String url) {
        this.uri = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String env) {
        this.profile = env;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Discovery getDiscovery() {
        return this.discovery;
    }

    public void setDiscovery(Discovery discovery) {
        this.discovery = discovery;
    }

    public boolean isFailFast() {
        return this.failFast;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }


    public static class Discovery {
        public static final String DEFAULT_CONFIG_SERVER = "config-center";

        /**
         * Flag to indicate that config server discovery is enabled (config server URL will be
         * looked up via discovery).
         */
        private boolean enabled;
        /**
         * Service id to locate config server.
         */
        private String serviceId = DEFAULT_CONFIG_SERVER;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getServiceId() {
            return this.serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

    }


    public boolean isLocalCacheEnbaled() {
        return localCacheEnbaled;
    }

    public void setLocalCacheEnbaled(boolean localCacheEnbaled) {
        this.localCacheEnbaled = localCacheEnbaled;
    }

    public long getLocalConfigCacheUpdateInterval() {
        return localConfigCacheUpdateInterval;
    }

    public void setLocalConfigCacheUpdateInterval(long localConfigCacheUpdateInterval) {
        this.localConfigCacheUpdateInterval = localConfigCacheUpdateInterval;
    }

    @Override
    public String toString() {
        return "ConfigClientProperties{" +
                "enabled=" + enabled +
                ", profile='" + profile + '\'' +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uri='" + uri + '\'' +
                ", discovery=" + discovery +
                ", failFast=" + failFast +
                ", localCacheEnbaled=" + localCacheEnbaled +
                ", localConfigCacheUpdateInterval=" + localConfigCacheUpdateInterval +
                '}';
    }
}
