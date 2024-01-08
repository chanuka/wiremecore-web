package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.CustomUserDetailsDao;
import com.cba.core.wiremeweb.dto.ApplicationUserDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserRole;
import com.cba.core.wiremeweb.model.UserType;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.util.DeviceTypeEnum;
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
    public ApplicationUserDto loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            UserType userType = new UserType();
            userType.setId(DeviceTypeEnum.WEB.getValue()); // only web users are allowed in this module

            User user = userRepository.findByUserNameAndUserType(userName, userType).orElseThrow(() -> new NotFoundException("User not found"));

            Set<SimpleGrantedAuthority> permissions = user.getUserRolesForUserId()
                    .stream()
                    .map(this::convertToSimpleGrant)
                    .collect(Collectors.toSet());

            return new ApplicationUserDto(user.getUserName(), user.getPassword(), permissions,
                    true, true, true, true);

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found cause is : " + e.getMessage());
        }

    }

    private SimpleGrantedAuthority convertToSimpleGrant(UserRole userrole) {
        return new SimpleGrantedAuthority("ROLE_" + userrole.getRole().getRoleName());
    }

}
