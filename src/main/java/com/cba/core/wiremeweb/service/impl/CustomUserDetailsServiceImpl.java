package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.impl.CustomUserDetailsDaoImpl;
import com.cba.core.wiremeweb.dto.ApplicationUserDto;
import com.cba.core.wiremeweb.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final CustomUserDetailsDaoImpl customUserDetailsDaoImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUserDto applicationUser = null;
        try {
            applicationUser = customUserDetailsDaoImpl.loadUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (applicationUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return applicationUser;
    }
}
