package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.model.TokenBlacklist;

public interface TokenBlacklistService {

    TokenBlacklist createBlacklistToken(String token) throws Exception;

    boolean isTokenBlacklisted(String token) throws Exception;
}
