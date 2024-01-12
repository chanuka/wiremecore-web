package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.model.TokenBlacklist;

public interface TokenBlacklistService {

    public TokenBlacklist createBlacklistToken(String token) throws Exception;

    public boolean isTokenBlacklisted(String token) throws Exception;
}
