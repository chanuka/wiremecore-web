package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.dto.RoleResourcePermissionDto;
import com.cba.core.wiremeweb.util.PaginationResponse;

import java.util.List;

public interface PermissionService<T, K> extends GenericService<T, K> {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws Exception;

    PaginationResponse<RoleResourcePermissionDto> findAllByUserRole(int page, int pageSize) throws Exception;

    void deleteByRole_Id(int roleId) throws Exception;

    void updateBulk(int roleId, List<K> requestDtoList) throws Exception;

}
