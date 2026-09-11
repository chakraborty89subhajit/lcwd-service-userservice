package com.lcwd.user.service.userservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class MyConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTamplate(){
        return new RestTemplate();

    }
}
