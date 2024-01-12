package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TokenBlacklistDao;
import com.cba.core.wiremeweb.model.TokenBlacklist;
import com.cba.core.wiremeweb.repository.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
