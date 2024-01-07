package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface TerminalResource<T, K> extends GenericResource<T, K> {

    @GetMapping("/all")
    ResponseEntity<List<TerminalResponseDto>> getAllTerminals() throws Exception;
}
