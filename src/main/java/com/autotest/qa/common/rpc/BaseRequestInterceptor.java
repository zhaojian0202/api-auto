package com.autotest.qa.common.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autotest.qa.common.ContextHolder;
import com.autotest.qa.kta.KTAConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign拦截器,作用在使用feign做服务间调用的时候，可以修改请求的头部或编码信息呢，可以通过实现RequestInterceptor接口的apply方法，
 * feign在发送请求之前都会调用该接口的apply方法，所以我们也可以通过实现该接口来记录请求发出去的时间点。
 *
 */
public class BaseRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        JSONObject object = JSON.parseObject("{\"timeZone\":\"GMT+8\",\"countryCode\":\"ID\",\"datasource\":\"kta1\",\"countryName\":\"Indonesia\",\"tenant\":\"KTAID\",\"currency\":\"CNY\"}");
        template.header("Tenant",object.toJSONString());
        template.header(KTAConstant.HEADER_CONTENT_LANGUAGE,"zh_CN");
        if (ContextHolder.getParmaByKey("Tenant") != null)
            template.header(KTAConstant.HEADER_TENANT, ContextHolder.getParmaByKey("Tenant").toString());
        if (ContextHolder.getParmaByKey("Cookie") != null)
            template.header(KTAConstant.HEADER_COOKIE, ContextHolder.getParmaByKey("Cookie").toString());
        if (ContextHolder.getParmaByKey("deviceInfo") != null)
            template.header(KTAConstant.HEADER_DEVICE_INFO, ContextHolder.getParmaByKey("deviceInfo").toString());
        if (ContextHolder.getParmaByKey("authorization") != null)
            template.header(KTAConstant.HEADER_AUTHORIZATION, ContextHolder.getParmaByKey("authorization").toString());
        if (ContextHolder.getParmaByKey("headerType") != null){
            template.header(KTAConstant.HEADER_CONTENT_TYPE,ContextHolder.getParmaByKey("headerType").toString());
        }else {
            template.header(KTAConstant.HEADER_CONTENT_TYPE,"application/json");
        }
    }
}
