package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.model.TokenRefresh;

import java.util.Optional;

public interface RefreshTokenService {

     Optional<TokenRefresh> findByToken(String token) throws Exception;

     TokenRefresh createRefreshToken(String userName) throws Exception;

     TokenRefresh verifyExpiration(TokenRefresh token);

     int deleteByUserId(Integer userId) throws Exception;
}
