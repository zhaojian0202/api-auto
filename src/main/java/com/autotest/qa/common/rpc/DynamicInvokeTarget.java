package com.autotest.qa.common.rpc;

import feign.Request;
import feign.RequestTemplate;
import feign.Target;

/**
 * 动态请求
 * @param <T>
 */
public class DynamicInvokeTarget<T> implements Target<T> {
    private final Class<T> type;
    private final String name="";
    private String apiHost;

    public DynamicInvokeTarget(Class<T> type, String apiHost) {
        this.type = type;
        this.apiHost = apiHost;
    }

    @Override
    public Class type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String url() {
        return apiHost;
    }

    @Override
    public Request apply(RequestTemplate input) {
        String method=input.method();
        String url=input.url();
        input.insert(0,apiHost);
        long timeMillis = System.currentTimeMillis();
        return input.request();
    }
}
