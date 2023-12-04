package com.cba.core.wiremeweb.dto;

import lombok.Data;

@Data
public class UsernameAndPasswordAuthenticationRequestDto {
    private String username;
    private String password;

    public UsernameAndPasswordAuthenticationRequestDto() {
    }
}
