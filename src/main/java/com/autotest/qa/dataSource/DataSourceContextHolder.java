package com.autotest.qa.dataSource;

import org.springframework.util.Assert;

public class DataSourceContextHolder {
    //线程本地环境
    private static final ThreadLocal<String> contextHolder=new ThreadLocal<>();
    //设置数据源类型
    public static void setDataSourceType(String dataSource){
        Assert.notNull(dataSource,"DataSourceType cannot be null");
        contextHolder.set(dataSource);
    }
    //获取数据源类型
    public static String getDataSourceType(){
        return contextHolder.get();
    }

    //清除数据源类型
    public static void clearDataSourceType(){
        contextHolder.remove();
    }
}
