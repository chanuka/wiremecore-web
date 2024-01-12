package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.TokenRefresh;
import com.cba.core.wiremeweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<TokenRefresh, Long> {
    Optional<TokenRefresh> findByToken(String token);

    int deleteByUserId(Integer userId);
}
