package com.autotest.qa.common.rpc;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


/**
 * 无法切入非spring管理的bean
 * 切面切不到SpringDecoder-暂时弃用
 */
@Aspect
@Slf4j
public class KTAInvokeAspect {


    @AfterReturning("execution(*org.springframework.cloud.openfeign.support.SpringDecoder.decode(..))")
    public void printLog(JoinPoint joinPoint) throws IOException {
        log.info("进入切面.....");
        Response response  = (Response) joinPoint.getArgs()[0];
        String bodyString = new BufferedReader(new InputStreamReader(response.body().asInputStream()))
                .lines()
                .parallel()
                .collect(Collectors.joining("\n"));
        String url = response.request().url();
        log.info("请求接口==>{},返回数据==>{}",url,bodyString);
    }
}