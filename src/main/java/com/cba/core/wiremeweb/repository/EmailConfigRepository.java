package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmailConfigRepository extends JpaRepository<EmailConfig, Integer>, JpaSpecificationExecutor<EmailConfig> {

    Optional<EmailConfig> findByAction(String action);

}
