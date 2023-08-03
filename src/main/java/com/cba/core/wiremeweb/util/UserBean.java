package com.cba.core.wiremeweb.util;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component // this is for storing user data through out the request scope
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserBean {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
