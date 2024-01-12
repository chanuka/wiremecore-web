package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.RefreshTokenDao;
import com.cba.core.wiremeweb.model.TokenRefresh;
import com.cba.core.wiremeweb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDaoImpl implements RefreshTokenDao {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<TokenRefresh> findByToken(String token) throws Exception {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public TokenRefresh createRefreshToken(TokenRefresh refreshToken) throws IOException {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public int deleteByUserId(Integer userId) throws Exception {
        return refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByToken(TokenRefresh token) {
        refreshTokenRepository.delete(token);
    }

}
