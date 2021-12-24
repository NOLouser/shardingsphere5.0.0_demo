package com.nolouser.config;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    public DataSource dataSource() {
        try {
            return YamlShardingSphereDataSourceFactory.createDataSource(new ClassPathResource("shardingsphere.yml").getFile());
        } catch (Exception throwables) {
            LOGGER.error("初始化配置文件【shardingsphere.yml】异常", throwables);
            throw new RuntimeException("初始化配置文件【shardingsphere.yml】异常", throwables);
        }
    }

}
