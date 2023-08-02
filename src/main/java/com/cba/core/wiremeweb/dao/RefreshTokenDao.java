package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.RefreshToken;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenDao {

    public Optional<RefreshToken> findByToken(String token) throws Exception;

    public RefreshToken createRefreshToken(String userName) throws IOException;

    public int deleteByUserId(Long userId) throws Exception;

    public RefreshToken verifyExpiration(RefreshToken token);
}
