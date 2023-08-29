package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserResource<T, K> extends GenericResource<T, K> {

    @PostMapping("/changePassword")
    ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) throws Exception;

}
