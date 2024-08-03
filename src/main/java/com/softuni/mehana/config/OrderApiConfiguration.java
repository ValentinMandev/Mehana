package com.softuni.mehana.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "orders.api")
public class OrderApiConfiguration {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public OrderApiConfiguration setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
