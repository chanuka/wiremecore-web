package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsDao {

    User loadUserByUsername(String username, UserType userType) throws UsernameNotFoundException;
}
