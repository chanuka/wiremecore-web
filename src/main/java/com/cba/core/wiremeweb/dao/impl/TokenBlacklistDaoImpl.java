package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TokenBlacklistDao;
import com.cba.core.wiremeweb.model.TokenBlacklist;
import com.cba.core.wiremeweb.repository.TokenBlacklistRepository;
import com.cba.core.wiremeweb.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class TokenBlacklistDaoImpl implements TokenBlacklistDao {

    private final TokenBlacklistRepository tokenBlacklistRepository;
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

        tokenBlacklist = tokenBlacklistRepository.save(tokenBlacklist);
        return tokenBlacklist;
    }

    @Override
    public boolean isTokenBlacklisted(String token) throws SQLException {
        Optional<TokenBlacklist> tokenBlacklist = tokenBlacklistRepository.findByToken(token);
        return tokenBlacklist != null
                && !tokenBlacklist.isEmpty()
                && tokenBlacklist.get().getExpiration().isAfter(Instant.now());
    }


    private Instant extractTokenExpiration(String token) throws Exception {
        Jwt claimsJws = jwtUtil.validateJwtToken(token, decoder);
        return claimsJws.getExpiresAt();
    }
}
