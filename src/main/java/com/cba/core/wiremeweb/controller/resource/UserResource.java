package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/${application.resource.users}")
public interface UserResource {

    @GetMapping
    ResponseEntity<List<UserResponseDto>> users(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> getAUser(@PathVariable(value = "id") int id) throws Exception;

    @GetMapping("/search")
    ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam(value = "userName") String userName,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int pageSize)  throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAUser(@PathVariable(value = "id") int id) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<UserResponseDto> updateAUser(@PathVariable(value = "id") int id,
                                                @Valid @RequestBody UserRequestDto userRequestDto) throws Exception;

    @PostMapping()
    ResponseEntity<UserResponseDto> createAUser(@Valid @RequestBody UserRequestDto userRequestDto) throws Exception;

    @PostMapping("/bulk")
    ResponseEntity<String> createUsers(@RequestBody List<UserRequestDto> list) throws Exception;

    @DeleteMapping("/bulkDelete")
    ResponseEntity<String> deleteUsers(@RequestBody List<Integer> userIdList) throws Exception;

    @GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadExcel() throws IOException;

    @GetMapping(value = "/download-pdf")
    ResponseEntity<byte[]> downloadJasper() throws Exception;
}
