package com.mine;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DruidDataSourceAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(DruidDataSourceAutoConfiguration.class);
    private DataSourceProperties dataSourceProperties;


    public DruidDataSourceAutoConfiguration(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean(initMethod = "init",destroyMethod = "close")
    public DataSource nonRefreshDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        initDruidDataSourceProperties(druidDataSource);
        return druidDataSource;

    }

    private void initDruidDataSourceProperties(DruidDataSource druidDataSource){
        druidDataSource.setDriverClassName(dataSourceProperties.getDriver());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        //其他参数
        druidDataSource.setInitialSize(dataSourceProperties.getInitialSize());
        druidDataSource.setMinIdle(dataSourceProperties.getMinIdle());
        druidDataSource.setMaxActive(dataSourceProperties.getMaxActive());
        druidDataSource.setMaxWait(dataSourceProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(dataSourceProperties.getValidationQuery());
        druidDataSource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSource.getMaxOpenPreparedStatements());
        logger.info("DruidDataSource init ok..........");
        logger.info(dataSourceProperties.toString());
    }





}
