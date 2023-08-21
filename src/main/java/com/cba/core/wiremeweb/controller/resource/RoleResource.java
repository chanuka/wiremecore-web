package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/${application.resource.roles}")
public interface RoleResource {

    @GetMapping
    ResponseEntity<List<RoleResponseDto>> roles(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/{id}")
    ResponseEntity<RoleResponseDto> getARole(@PathVariable(value = "id") int id) throws Exception;

    @GetMapping("/search")
    ResponseEntity<List<RoleResponseDto>> searchRoles(@RequestParam(value = "roleName") String roleName,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int pageSize)  throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteARole(@PathVariable(value = "id") int id) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<RoleResponseDto> updateARole(@PathVariable(value = "id") int id,
                                                @Valid @RequestBody RoleRequestDto roleRequestDto) throws Exception;

    @PostMapping()
    ResponseEntity<RoleResponseDto> createARole(@Valid @RequestBody RoleRequestDto roleRequestDto) throws Exception;

    @PostMapping("/bulk")
    ResponseEntity<String> createRoles(@RequestBody List<RoleRequestDto> list) throws Exception;

    @DeleteMapping("/bulkDelete")
    ResponseEntity<String> deleteRoles(@RequestBody List<Integer> roleIdList) throws Exception;

    @GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadExcel() throws IOException;

    @GetMapping(value = "/download-pdf")
    ResponseEntity<byte[]> downloadJasper() throws Exception;
}
