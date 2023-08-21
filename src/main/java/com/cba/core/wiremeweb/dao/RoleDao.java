package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleDao {

    Page<RoleResponseDto> findAll(int page, int pageSize) throws Exception;

    List<RoleResponseDto> findAll() throws Exception;

    Page<RoleResponseDto> findByRoleNameLike(String roleName, int page, int pageSize) throws Exception;

    RoleResponseDto findById(int id) throws Exception;

    RoleResponseDto deleteById(int id) throws Exception;

    void deleteByIdList(List<Integer> roleList) throws Exception;

    RoleResponseDto updateById(int id, RoleRequestDto roleRequestDto) throws Exception;

    RoleResponseDto create(RoleRequestDto roleRequestDto) throws Exception;

    List<RoleResponseDto> createBulk(List<RoleRequestDto> roleRequestDtoList) throws Exception;

}
