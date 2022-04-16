package com.autotest.qa;

import com.autotest.qa.annotations.EnableTestingListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableTestingListener
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
