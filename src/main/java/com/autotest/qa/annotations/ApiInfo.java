package com.autotest.qa.annotations;

import com.autotest.qa.dao.RequestEnum;

import java.lang.annotation.*;

/**
 * 接口信息自定义注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiInfo {
    //请求路径
    String path();
    //请求方式
    RequestEnum method();

    //是否需要登录
    boolean isNeedLogin() default false;

    //登录账号
    String mobile() default "12345678901";

    //登录密码
    String password() default "Passw0rd";

    //需要保存的返回参数
    String[] results() default "";
}
