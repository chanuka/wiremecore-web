package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    public static UserResponseDto toDto(User entity) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setName(entity.getName());
        responseDto.setUserName(entity.getUserName());
        responseDto.setId(entity.getId());
        responseDto.setContactNo(entity.getContactNo());
        responseDto.setEmail(entity.getEmail());
        return responseDto;
    }

    public static User toModel(UserRequestDto requestDto) {
        User entity = new User();
        entity.setName(requestDto.getName());
        entity.setPassword(new BCryptPasswordEncoder().encode(requestDto.getPassword()));
        entity.setUserName(requestDto.getUserName());
        entity.setContactNo(requestDto.getContactNo());
        entity.setEmail(requestDto.getEmail());
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setUserType(new UserType(requestDto.getUserType()));
        return entity;
    }

}
