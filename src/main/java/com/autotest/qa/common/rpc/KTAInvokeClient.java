package com.autotest.qa.common.rpc;


import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import feign.Feign;
import feign.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;


public class KTAInvokeClient {

    public static ObjectFactory<HttpMessageConverters>  buildFastJsonConverter(){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter=new FastJsonHttpMessageConverter();
        HttpMessageConverters messageConverters=new HttpMessageConverters(fastJsonHttpMessageConverter);
        return () -> messageConverters;

    }

    /**
     *创建可以调用的api对象
     * @param url
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T buildApi(String url,Class<T> clazz){
        Object service=null;
        if (clazz!=null){
           service= Feign.builder().logLevel(Logger.Level.FULL).logger(new Logger.JavaLogger()).contract(new SpringMvcContract())
                   .encoder(new SpringEncoder(buildFastJsonConverter()))
                   .decoder(new TraceDecoder(buildFastJsonConverter()))
                   .requestInterceptor(new BaseRequestInterceptor())
                   .target(new DynamicInvokeTarget<>(clazz,url));
        }
        return (T) service;
    }

}
