package com.cba.core.wiremeweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.refresh")
public class RefreshTokenConfig {

    private Long tokenExpirationAfterMillis;

    public Long getTokenExpirationAfterMillis() {
        return tokenExpirationAfterMillis;
    }

    public void setTokenExpirationAfterMillis(Long tokenExpirationAfterMillis) {
        this.tokenExpirationAfterMillis = tokenExpirationAfterMillis;
    }
}
