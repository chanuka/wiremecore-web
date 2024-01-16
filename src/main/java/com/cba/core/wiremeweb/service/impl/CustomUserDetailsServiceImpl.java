package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.CustomUserDetailsDao;
import com.cba.core.wiremeweb.dto.ApplicationUserDto;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserRole;
import com.cba.core.wiremeweb.model.UserType;
import com.cba.core.wiremeweb.service.CustomUserDetailsService;
import com.cba.core.wiremeweb.util.DeviceTypeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional // has to add in order to overcome lazy loading issue
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final CustomUserDetailsDao customUserDetailsDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            UserType userType = new UserType();
            userType.setId(DeviceTypeEnum.WEB.getValue()); // only web users are allowed in this module

            User user = customUserDetailsDao.loadUserByUsername(userName, userType);

            Set<SimpleGrantedAuthority> permissions = user.getUserRolesForUserId()
                    .stream()
                    .map(this::convertToSimpleGrant)
                    .collect(Collectors.toSet());

            return new ApplicationUserDto(user.getUserName(), user.getPassword(), permissions,
                    true, true, true, true);

        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }
    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }
}
