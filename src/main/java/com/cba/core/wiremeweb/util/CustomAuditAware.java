package com.cba.core.wiremeweb.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CustomAuditAware implements AuditorAware<String> { // use for auditing purpose, enable created and modified user at entity level

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
//        return Optional.of(((User) authentication.getPrincipal()).getUserName());
        return Optional.of((authentication.getPrincipal()).toString());
    }
}
