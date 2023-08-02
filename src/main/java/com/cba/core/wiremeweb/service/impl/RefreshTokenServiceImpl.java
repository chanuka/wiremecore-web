package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.impl.RefreshTokenDaoImpl;
import com.cba.core.wiremeweb.model.RefreshToken;
import com.cba.core.wiremeweb.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenDaoImpl refreshTokenDaoImpl;

    @Override
    public Optional<RefreshToken> findByToken(String token) throws Exception {
        return refreshTokenDaoImpl.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String userName) throws IOException {
        RefreshToken refreshToken = refreshTokenDaoImpl.createRefreshToken(userName);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {// need to add exception handling
        token = refreshTokenDaoImpl.verifyExpiration(token);
        return token;
    }

    @Override
    public int deleteByUserId(Long userId) throws Exception {
        return refreshTokenDaoImpl.deleteByUserId((userId));
    }
}
