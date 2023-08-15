package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.CustomUserDetailsDao;
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

    private final CustomUserDetailsDao customUserDetailsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            ApplicationUserDto applicationUser = customUserDetailsDao.loadUserByUsername(username);
            return applicationUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
