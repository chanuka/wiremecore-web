package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.model.TokenRefresh;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenService {

    public Optional<TokenRefresh> findByToken(String token) throws Exception;

    public TokenRefresh createRefreshToken(String userName) throws IOException;

    public TokenRefresh verifyExpiration(TokenRefresh token);

    public int deleteByUserId(Integer userId) throws Exception;
}
