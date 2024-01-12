package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.TokenBlacklistDao;
import com.cba.core.wiremeweb.model.TokenBlacklist;
import com.cba.core.wiremeweb.service.TokenBlacklistService;
import com.cba.core.wiremeweb.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final TokenBlacklistDao dao;
    private final JwtUtil jwtUtil;
    private final JwtDecoder decoder;

    @Override
    public TokenBlacklist createBlacklistToken(String token) throws Exception {
        TokenBlacklist tokenBlacklist = null;
        if (isTokenBlacklisted(token)) {
            return tokenBlacklist;
        }
        tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setExpiration(extractTokenExpiration(token));

        tokenBlacklist = dao.create(tokenBlacklist);
        return tokenBlacklist;

    }

    @Override
    public boolean isTokenBlacklisted(String token) throws Exception {
        try {
            TokenBlacklist tokenBlacklist = dao.findByToken(token);
            return tokenBlacklist != null
                    && tokenBlacklist.getExpiration().isAfter(Instant.now());
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    private Instant extractTokenExpiration(String token) throws Exception {
        Jwt claimsJws = jwtUtil.validateJwtToken(token, decoder);
        return claimsJws.getExpiresAt();
    }
}
