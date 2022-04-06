package com.autotest.qa.common.rpc;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
@Slf4j
public class TraceDecoder extends SpringDecoder {

    public TraceDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        String bodyString=new BufferedReader(new InputStreamReader(response.body().asInputStream()))
                .lines()
                .parallel()
                .collect(Collectors.joining("\n"));
        String url=response.request().url();
        log.info("请求接口==>{},返回数据==>{}",url,bodyString);
        Response response1=response.toBuilder().body(bodyString, StandardCharsets.UTF_8).build();
        return super.decode(response1,type);
    }
}
