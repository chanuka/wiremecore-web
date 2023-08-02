package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.CustomUserDetailsDao;
import com.cba.core.wiremeweb.dto.ApplicationUserDto;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserRole;
import com.cba.core.wiremeweb.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional // has to add in order to overcome lazy loading issue
@RequiredArgsConstructor
public class CustomUserDetailsDaoImpl implements CustomUserDetailsDao {

    private final UserRepository userRepository;

    @Override
    public ApplicationUserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepository.findByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<SimpleGrantedAuthority> permissions = null;
        permissions = user.getUserRolesForUserId()
                .stream()
                .map(this::convertToSimpleGrant)
                .collect(Collectors.toSet());

        return new ApplicationUserDto(user.getUserName(), user.getPassword(), permissions,
                true, true, true, true);
    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }

}
