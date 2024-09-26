package com.hmall.trade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author:kim
 * @Description: TODO
 * @DateTime: 2024/9/13 17:26
 **/
@Configuration
public class RemoteCallConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
