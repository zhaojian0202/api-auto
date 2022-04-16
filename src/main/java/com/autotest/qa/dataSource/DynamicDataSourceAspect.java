package com.autotest.qa.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.autotest.qa.annotations.UseDataBase;
import com.autotest.qa.utils.PropertiesUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;

/**
 * 动态数据源切面
 */
@Aspect
@Order(1)
public class DynamicDataSourceAspect {

    /**
     * 切面选择需要切换的数据库
     * @param joinPoint
     */
    @Around("@annotation(com.autotest.qa.annotations.UseDataBase)")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable{
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        Method m = getMethod(joinPoint, args);
        //获取数据库名称参数
        UseDataBase chooseUseDataBase = m.getAnnotation(UseDataBase.class);
        if(chooseUseDataBase != null){
            String dataSourceName = chooseUseDataBase.value();
            DataSourceContextHolder.setDataSourceType(dataSourceName);
            System.out.println("当前数据库为:"+dataSourceName);

        }
        try {
            return joinPoint.proceed();
        }finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }


    /**
     * 获取切面的方法
     * @param joinPoint
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(ProceedingJoinPoint joinPoint, Object[] args) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class clazz = joinPoint.getTarget().getClass();
        Method[] methods = clazz.getMethods();
        for(Method method : methods) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

}
