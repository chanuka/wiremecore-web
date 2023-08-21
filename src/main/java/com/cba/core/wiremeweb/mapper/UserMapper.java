package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;

public class UserMapper {
    public static UserResponseDto toDto(User user) {
        UserResponseDto deviceResponseDto = new UserResponseDto();
        deviceResponseDto.setName(user.getName());
        deviceResponseDto.setUserName(user.getUserName());
        deviceResponseDto.setId(user.getId());
        deviceResponseDto.setContactNo(user.getContactNo());
        deviceResponseDto.setEmail(user.getEmail());
        return deviceResponseDto;
    }

    public static User toModel(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setUserName(userRequestDto.getUserName());
        user.setContactNo(userRequestDto.getContactNo());
        user.setEmail(userRequestDto.getEmail());
        user.setStatus(new Status(userRequestDto.getStatus()));
        user.setUserType(new UserType(userRequestDto.getUserType()));
        return user;
    }
}
