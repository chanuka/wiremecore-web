package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.impl.TokenBlacklistDaoImpl;
import com.cba.core.wiremeweb.model.TokenBlacklist;
import com.cba.core.wiremeweb.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final TokenBlacklistDaoImpl tokenBlacklistDaoImpl;

    @Override
    public TokenBlacklist createBlacklistToken(String token) throws Exception {
        TokenBlacklist tokenBlacklist = tokenBlacklistDaoImpl.createBlacklistToken(token);
        return tokenBlacklist;
    }

    @Override
    public boolean isTokenBlacklisted(String token) throws SQLException {
        boolean isTokenBlackListed = tokenBlacklistDaoImpl.isTokenBlacklisted(token);
        return isTokenBlackListed;
    }
}
