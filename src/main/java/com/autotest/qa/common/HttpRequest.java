package com.autotest.qa.common;


import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpRequest {

    public static ResponseEntity<String> senRequest(String url, HttpHeaders httpHeaders, Object params){
        RestTemplate restTemplate=new RestTemplate();
        HttpEntity<Object> requestEntity=new HttpEntity<>(params,httpHeaders);
        ResponseEntity<String> response=restTemplate.exchange(url, HttpMethod.POST,requestEntity,String.class);
        if (HttpStatus.OK.equals(response.getStatusCode())){
            return response;
        }else {
            System.err.println("接口请求失败，返回码为："+response.getStatusCode().value());
            return response;
        }
    }

    public static void Headers(){
        HttpHeaders httpHeaders=new HttpHeaders();
    }
}
