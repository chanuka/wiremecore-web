package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/${application.resource.devices}")
public interface DeviceResource {

    @GetMapping
    ResponseEntity<List<DeviceResponseDto>> devices(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/{id}")
    ResponseEntity<DeviceResponseDto> getADevice(@PathVariable(value = "id") int id) throws Exception;

    @GetMapping("/search")
    ResponseEntity<List<DeviceResponseDto>> searchDevices(@RequestParam(value = "serialNumber") String serialNumber,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int pageSize)  throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteADevice(@PathVariable(value = "id") int id) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<DeviceResponseDto> updateADevice(@PathVariable(value = "id") int id,
                                                    @Valid @RequestBody DeviceRequestDto deviceRequestDto) throws Exception;

    @PostMapping()
    ResponseEntity<DeviceResponseDto> createADevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) throws Exception;

    @PostMapping("/bulk")
    ResponseEntity<String> createDevices(@RequestBody List<DeviceRequestDto> list) throws Exception;

    @DeleteMapping("/bulkDelete")
    ResponseEntity<String> deleteDevices(@RequestBody List<Integer> deviceIdList) throws Exception;

    @GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadExcel() throws IOException;

    @GetMapping(value = "/download-pdf")
    ResponseEntity<byte[]> downloadJasper() throws Exception;

}
