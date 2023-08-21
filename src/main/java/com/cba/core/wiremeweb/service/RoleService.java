package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;

import java.io.FileNotFoundException;
import java.util.List;

public interface RoleService {

    Page<RoleResponseDto> findAll(int page, int pageSize) throws Exception;

    List<RoleResponseDto> findAll() throws Exception;

    Page<RoleResponseDto> findByRoleNameLike(String roleName, int page, int pageSize) throws Exception;

    RoleResponseDto findById(int id) throws Exception;

    RoleResponseDto deleteById(int id) throws Exception;

    void deleteByIdList(List<Integer> roleList) throws Exception;

    RoleResponseDto updateById(int id, RoleRequestDto roleRequestDto) throws Exception;

    RoleResponseDto create(RoleRequestDto roleRequestDto) throws Exception;

    List<RoleResponseDto> createBulk(List<RoleRequestDto> roleRequestDtoList) throws Exception;

    byte[] exportReport() throws Exception;

}
