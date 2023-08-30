package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface ResourceResource {

    @GetMapping
    ResponseEntity<List<ResourceResponseDto>> getAll() throws Exception;
}
