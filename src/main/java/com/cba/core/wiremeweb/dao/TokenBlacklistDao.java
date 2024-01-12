package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.TokenBlacklist;

public interface TokenBlacklistDao {

     TokenBlacklist create(TokenBlacklist token) throws Exception;

     TokenBlacklist findByToken(String token) throws Exception;

}
