package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface PermissionResource<T, K> extends GenericResource<T, K> {

    @GetMapping("/all")
    ResponseEntity<List<PermissionResponseDto>> findAllPermissionsByUser() throws Exception;

}
