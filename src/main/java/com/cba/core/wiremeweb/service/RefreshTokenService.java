package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.model.RefreshToken;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenService {

    public Optional<RefreshToken> findByToken(String token) throws Exception;

    public RefreshToken createRefreshToken(String userName) throws IOException;

    public RefreshToken verifyExpiration(RefreshToken token);

    public int deleteByUserId(Long userId) throws Exception;
}
