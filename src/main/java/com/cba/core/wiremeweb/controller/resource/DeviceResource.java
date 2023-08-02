package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devices")
public interface DeviceResource {

    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> devices(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getADevice(@PathVariable(value = "id") int id) throws Exception;

    @GetMapping("/search")
    public ResponseEntity<List<DeviceResponseDto>> searchDevices(@RequestParam(value = "serialNumber") String serialNumber,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int pageSize)  throws Exception;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteADevice(@PathVariable(value = "id") int id) throws Exception;

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> updateADevice(@PathVariable(value = "id") int id,
                                                           @RequestBody DeviceRequestDto deviceRequestDto) throws Exception;

    @PostMapping()
    public ResponseEntity<DeviceResponseDto> createADevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) throws Exception;

    @PostMapping("/bulk")
    public ResponseEntity<String> createDevices(@RequestBody List<DeviceRequestDto> list) throws Exception;

    @DeleteMapping("/bulkDelete")
    public ResponseEntity<String> deleteDevices(@RequestBody List<Map<String, Integer>> deviceIdList) throws Exception;

    @GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadExcel() throws IOException;

    @GetMapping(value = "/download-pdf")
    public ResponseEntity<byte[]> downloadJasper() throws Exception;

}
