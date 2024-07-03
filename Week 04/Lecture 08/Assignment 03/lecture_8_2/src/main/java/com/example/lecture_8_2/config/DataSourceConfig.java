package com.example.lecture_8_2.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource1")
    public HikariConfig hikariConfig1() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource1() {
        return new HikariDataSource(hikariConfig1());
    }

    @Bean
    public DataSourceTransactionManager transactionManager1(@Qualifier("dataSource1") DataSource dataSource1) {
        return new DataSourceTransactionManager(dataSource1);
    }

    @Bean
    @ConfigurationProperties("spring.datasource2")
    public HikariConfig hikariConfig2() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource2() {
        return new HikariDataSource(hikariConfig2());
    }

    @Bean
    public DataSourceTransactionManager transactionManager2(@Qualifier("dataSource2") DataSource dataSource2) {
        return new DataSourceTransactionManager(dataSource2);
    }

    @SuppressWarnings("deprecation")
    @Bean
    @Primary
    public PlatformTransactionManager chainedTransactionManager(
            @Qualifier("transactionManager1") DataSourceTransactionManager transactionManager1,
            @Qualifier("transactionManager2") DataSourceTransactionManager transactionManager2) {
        return new ChainedTransactionManager(transactionManager1, transactionManager2);
    }

    @Bean
    @Qualifier("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1(@Qualifier("dataSource1") DataSource dataSource1) {
        return new JdbcTemplate(dataSource1);
    }

    @Bean
    @Qualifier("jdbcTemplate2")
    public JdbcTemplate jdbcTemplate2(@Qualifier("dataSource2") DataSource dataSource2) {
        return new JdbcTemplate(dataSource2);
    }
}