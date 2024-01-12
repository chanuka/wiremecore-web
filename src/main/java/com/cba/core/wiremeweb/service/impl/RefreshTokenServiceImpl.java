package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.config.RefreshTokenConfig;
import com.cba.core.wiremeweb.dao.RefreshTokenDao;
import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.exception.TokenRefreshException;
import com.cba.core.wiremeweb.model.TokenRefresh;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenDao refreshTokenDao;
    private final UserDao<UserResponseDto, UserRequestDto> userDao;
    private final RefreshTokenConfig refreshTokenConfig;


    @Override
    public Optional<TokenRefresh> findByToken(String token) throws Exception {
        return refreshTokenDao.findByToken(token);
    }

    @Override
    public TokenRefresh createRefreshToken(String userName) throws Exception {

        TokenRefresh refreshToken = new TokenRefresh();
        User user = userDao.findByUserName(userName);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenConfig.getTokenExpirationAfterMillis()));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenDao.createRefreshToken(refreshToken);
        return refreshToken;
    }

    @Override
    public TokenRefresh verifyExpiration(TokenRefresh token) {// need to add exception handling
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenDao.deleteByToken(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }

    @Override
    public int deleteByUserId(Integer userId) throws Exception {
        return refreshTokenDao.deleteByUserId((userId));
    }
}
