package com.autotest.qa.config;

import com.alibaba.druid.pool.DruidDataSource;

import com.autotest.qa.dataSource.DruidDataSourceFactory;
import com.autotest.qa.dataSource.DynamicDataSource;
import com.autotest.qa.utils.PropertiesUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DBConfig {


    private static final String dbHost  = PropertiesUtil.getEnv("mysql.host");
    private static final String dbname = PropertiesUtil.getEnv("mysql.user");
    private static final String dbpass = PropertiesUtil.getEnv("mysql.pass");


    public DruidDataSource aggDb() throws SQLException {
        return DruidDataSourceFactory.getSimpleDruidDataSource("jdbc:mysql://"+dbHost+"/finance_aggregation?useSSL=false", dbname, dbpass);
    }

    public DruidDataSource userDb() throws SQLException {
        return DruidDataSourceFactory.getSimpleDruidDataSource("jdbc:mysql://"+dbHost+"/finance_user?useSSL=false", dbname, dbpass);
    }

    public DruidDataSource loanDb() throws SQLException {
        return DruidDataSourceFactory.getSimpleDruidDataSource("jdbc:mysql://"+dbHost+"/finance_loan?useSSL=false", dbname, dbpass);
    }

    public DruidDataSource dataDb() throws SQLException{
        return DruidDataSourceFactory.getSimpleDruidDataSource("jdbc:mysql://"+dbHost+"/finance_data?useSSL=false",dbname,dbpass);
    }


    @Bean(name="dynamicDataSource")
    public DataSource dataSource() throws Exception {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(aggDb());
        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("aggDb", aggDb());
        dsMap.put("userDb", userDb());
        dsMap.put("loanDb",loanDb());
        dsMap.put("dataDb",dataDb());
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

}
