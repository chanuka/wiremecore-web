package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;

import java.io.FileNotFoundException;
import java.util.List;

public interface UserService {

     Page<UserResponseDto> findAll(int page, int pageSize) throws Exception;

     List<UserResponseDto> findAll() throws Exception;

     Page<UserResponseDto> findByUserNameLike(String userName,int page, int pageSize) throws Exception;

     UserResponseDto findById(int id) throws Exception;

     UserResponseDto deleteById(int id) throws Exception;

     void deleteByIdList(List<Integer> userList) throws Exception;

     UserResponseDto updateById(int id, UserRequestDto userRequestDto) throws Exception;

     UserResponseDto create(UserRequestDto userRequestDto) throws Exception;

     List<UserResponseDto> createBulk(List<UserRequestDto> userRequestDtoList) throws Exception;

     byte[] exportReport() throws Exception;
}
