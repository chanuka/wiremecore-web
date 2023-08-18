package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.RefreshTokenDao;
import com.cba.core.wiremeweb.model.TokenRefresh;
import com.cba.core.wiremeweb.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenDao refreshTokenDao;

    @Override
    public Optional<TokenRefresh> findByToken(String token) throws Exception {
        return refreshTokenDao.findByToken(token);
    }

    @Override
    public TokenRefresh createRefreshToken(String userName) throws IOException {
        TokenRefresh refreshToken = refreshTokenDao.createRefreshToken(userName);
        return refreshToken;
    }

    @Override
    public TokenRefresh verifyExpiration(TokenRefresh token) {// need to add exception handling
        token = refreshTokenDao.verifyExpiration(token);
        return token;
    }

    @Override
    public int deleteByUserId(Long userId) throws Exception {
        return refreshTokenDao.deleteByUserId((userId));
    }
}
