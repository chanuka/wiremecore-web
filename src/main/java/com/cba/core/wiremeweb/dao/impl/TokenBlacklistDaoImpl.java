package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TokenBlacklistDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.TokenBlacklist;
import com.cba.core.wiremeweb.repository.TokenBlacklistRepository;
import com.cba.core.wiremeweb.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenBlacklistDaoImpl implements TokenBlacklistDao {

    private final TokenBlacklistRepository repository;

    @Override
    public TokenBlacklist create(TokenBlacklist token) throws Exception {
        return repository.save(token);
    }

    @Override
    public TokenBlacklist findByToken(String token) throws Exception {
        return repository.findByToken(token).orElse(null);
    }


}
