package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.PermissionRequestDto;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.dto.RoleResourcePermissionDto;
import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.util.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PermissionResource<T, K> extends GenericResource<T, K> {

    @GetMapping("/all")
    ResponseEntity<List<PermissionResponseDto>> findAllPermissions() throws Exception;

    @GetMapping("/allByRole")
    ResponseEntity<PaginationResponse<RoleResourcePermissionDto>> findAllPermissionsByUserRole(@RequestParam(defaultValue = "0") int page,
                                                                                               @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @DeleteMapping("/byRole/{id}")
    ResponseEntity<String> deleteByRole(@PathVariable(value = "id") int id) throws Exception;

    @PutMapping("/bulk/{roleId}")
    ResponseEntity<String> updateBulk(@PathVariable(value = "roleId") int roleId,
                                      @RequestBody List<PermissionRequestDto> requestDtoList) throws Exception;

}
