package com.nolouser.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    public DataSource dataSource() {
        try {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            druidDataSource.setUrl("jdbc:mysql://localhost:3306/month_ds?useUnicode=true&characterEncoding=utf-8&useSSL=false");
            druidDataSource.setUsername("root");
            druidDataSource.setPassword("123456");

            Map<String, DataSource> dataSourceMap = new HashMap<>();
            dataSourceMap.put("ds", druidDataSource);


            return YamlShardingSphereDataSourceFactory.createDataSource(dataSourceMap, new ClassPathResource("shardingsphere.yml").getFile());
        } catch (Exception throwables) {
            LOGGER.error("初始化配置文件【shardingsphere.yml】异常", throwables);
            throw new RuntimeException("初始化配置文件【shardingsphere.yml】异常", throwables);
        }
    }

}
