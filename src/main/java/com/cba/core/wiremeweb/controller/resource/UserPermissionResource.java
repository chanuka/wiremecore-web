package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public interface UserPermissionResource {

    @GetMapping()
    public ResponseEntity<List<PermissionResponseDto>> permission() throws Exception;
}
