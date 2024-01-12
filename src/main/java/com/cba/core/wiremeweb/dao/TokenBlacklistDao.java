package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.TokenBlacklist;

import java.sql.SQLException;

public interface TokenBlacklistDao {

     TokenBlacklist create(TokenBlacklist token) throws Exception;

     TokenBlacklist findByToken(String token) throws Exception;

}
