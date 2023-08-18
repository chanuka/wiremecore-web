package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.UserPermissionResource;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@RequiredArgsConstructor
@Validated
public class UserPermissionController implements UserPermissionResource {

    private final UserPermissionService userPermissionService;

    @Override
    public ResponseEntity<List<PermissionResponseDto>> permission() throws Exception {

        List<PermissionResponseDto> list = null;
        try {
            list = userPermissionService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(list);
    }
}
