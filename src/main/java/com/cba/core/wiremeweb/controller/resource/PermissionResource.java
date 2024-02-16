package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.dto.RoleResourcePermissionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PermissionResource<T, K> extends GenericResource<T, K> {

    @GetMapping("/all")
    ResponseEntity<List<PermissionResponseDto>> findAllPermissions() throws Exception;

    @GetMapping("/allByRole")
    ResponseEntity<List<RoleResourcePermissionDto>> findAllPermissionsByUserRole() throws Exception;

    @DeleteMapping("/byRole/{id}")
    ResponseEntity<String> deleteByRole(@PathVariable(value = "id") int id) throws Exception;



}
