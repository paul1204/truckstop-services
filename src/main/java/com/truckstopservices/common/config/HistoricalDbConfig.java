package com.truckstopservices.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class HistoricalDbConfig {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "historicalDataSource")
    @ConfigurationProperties("app.historical.datasource")
    public DataSource historicalDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "historicalJdbcTemplate")
    public NamedParameterJdbcTemplate historicalJdbcTemplate(
            @Qualifier("historicalDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
