package com.cba.core.wiremeweb.dto;

public class UsernameAndPasswordAuthenticationRequestDto {
    private String username;
    private String password;

    public UsernameAndPasswordAuthenticationRequestDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
