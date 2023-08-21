package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserDao {

    public Page<UserResponseDto> findAll(int page, int pageSize) throws Exception;

    public List<UserResponseDto> findAll() throws Exception;

    public Page<UserResponseDto> findByUserNameLike(String userName, int page, int pageSize) throws Exception;

    public UserResponseDto findById(int id) throws Exception;

    public UserResponseDto deleteById(int id) throws Exception;

    public void deleteByIdList(List<Integer> userList) throws Exception;

    public UserResponseDto updateById(int id, UserRequestDto userRequestDto) throws Exception;

    public UserResponseDto create(UserRequestDto userRequestDto) throws Exception;

    public List<UserResponseDto> createBulk(List<UserRequestDto> userRequestDtoList) throws Exception;
}
