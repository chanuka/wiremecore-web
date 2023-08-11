package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.config.RefreshTokenConfig;
import com.cba.core.wiremeweb.dao.RefreshTokenDao;
import com.cba.core.wiremeweb.exception.TokenRefreshException;
import com.cba.core.wiremeweb.model.TokenRefresh;
import com.cba.core.wiremeweb.repository.RefreshTokenRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class RefreshTokenDaoImpl implements RefreshTokenDao {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenConfig refreshTokenConfig;
    private final UserRepository userRepository;

    @Override
    public Optional<TokenRefresh> findByToken(String token) throws Exception {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public TokenRefresh createRefreshToken(String userName) throws IOException {
        TokenRefresh refreshToken = new TokenRefresh();
        refreshToken.setUser(userRepository.findByUserName(userName));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenConfig.getTokenExpirationAfterMillis()));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public int deleteByUserId(Long userId) throws Exception {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Override
    public TokenRefresh verifyExpiration(TokenRefresh token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }
}
