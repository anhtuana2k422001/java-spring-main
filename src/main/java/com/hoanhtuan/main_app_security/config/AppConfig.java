package com.hoanhtuan.main_app_security.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class AppConfig {
    @Value("${app.domain}")
    private String domain;

    @Value("${app.api.url}")
    private String apiUrl;

    @PostConstruct
    public void init() {
        LOG.info("Domain: {}", domain);
        LOG.info("API URL: {}", apiUrl);
    }
}
