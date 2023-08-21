package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.TokenRefreshRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refreshtoken")
public interface RefreshTokenResource {

    @PostMapping()
    ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) throws Exception;
}
