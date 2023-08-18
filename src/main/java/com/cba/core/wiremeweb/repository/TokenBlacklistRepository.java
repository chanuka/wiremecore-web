package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Integer> {

    Optional<TokenBlacklist> findByToken(String token);
}
