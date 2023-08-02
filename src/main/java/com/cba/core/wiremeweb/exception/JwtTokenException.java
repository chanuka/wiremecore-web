package com.cba.core.wiremeweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This is to handle token level exceptions
 * @see com.cba.core.wiremeweb.security.SecurityConfig
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtTokenException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public JwtTokenException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
