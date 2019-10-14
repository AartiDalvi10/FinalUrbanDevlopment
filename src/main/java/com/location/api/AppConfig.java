package com.location.api;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	 @Bean
	 public RestTemplate restTemplate(RestTemplateBuilder builder) {

	        return builder
	                .setConnectTimeout(5000)
	                .setReadTimeout(5000)
	                .build();
	    }
	
	
}