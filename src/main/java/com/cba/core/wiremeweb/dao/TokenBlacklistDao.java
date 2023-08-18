package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.TokenBlacklist;

import java.sql.SQLException;

public interface TokenBlacklistDao {

    public TokenBlacklist createBlacklistToken(String token) throws Exception;

    public boolean isTokenBlacklisted(String token) throws SQLException;

}
