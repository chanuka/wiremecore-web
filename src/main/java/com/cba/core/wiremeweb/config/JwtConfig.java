package com.cba.core.wiremeweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterMillis;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterMillis() {
        return tokenExpirationAfterMillis;
    }

    public void setTokenExpirationAfterMillis(Integer tokenExpirationAfterMillis) {
        this.tokenExpirationAfterMillis = tokenExpirationAfterMillis;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
