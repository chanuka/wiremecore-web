package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.ApplicationUserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsDao {

    ApplicationUserDto loadUserByUsername(String username) throws UsernameNotFoundException;
}
