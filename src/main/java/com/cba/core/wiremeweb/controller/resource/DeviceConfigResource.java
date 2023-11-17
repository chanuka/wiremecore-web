package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public interface DeviceConfigResource {

    @GetMapping("/{id}")
    ResponseEntity<DeviceConfigResponseDto> getOne(@PathVariable int id) throws Exception;

    @PostMapping()
    ResponseEntity<DeviceConfigResponseDto> createOne(@Valid @RequestBody DeviceConfigRequestDto requestDto) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<DeviceConfigResponseDto> updateOne(@Valid @RequestBody DeviceConfigRequestDto requestDto,
                                                      @PathVariable(value = "id") int id) throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteOne(@PathVariable(value = "id") int id) throws Exception;

}
