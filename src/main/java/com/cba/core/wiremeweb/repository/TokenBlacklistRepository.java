package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Integer> {

    Optional<TokenBlacklist> findByToken(String token);
}
