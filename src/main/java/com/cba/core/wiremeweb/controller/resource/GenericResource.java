package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.util.PaginationResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public interface GenericResource<T,K> {

    @GetMapping
    ResponseEntity<PaginationResponse<T>> getAllByPageWise(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/{id}")
    ResponseEntity<T> getOne(@PathVariable int id) throws Exception;

    @PostMapping("/search")
    ResponseEntity<PaginationResponse<T>> searchAllByPageWise(@RequestBody Map<String, String> searchParamList,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int pageSize)  throws Exception;

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteOne(@PathVariable(value = "id") int id) throws Exception;

    @PutMapping("/{id}")
    ResponseEntity<T> updateOne(@PathVariable(value = "id") int id,
                                                    @Valid @RequestBody K requestDto) throws Exception;

    @PostMapping()
    ResponseEntity<T> createOne(@Valid @RequestBody K requestDto) throws Exception;

    @PostMapping("/bulk")
    ResponseEntity<String> createBulk(@RequestBody List<K> requestDtoList) throws Exception;

    @DeleteMapping("/bulkDelete")
    ResponseEntity<String> deleteBulkByIdList(@RequestBody List<Integer> idList) throws Exception;

    @GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadExcel() throws Exception;

    @GetMapping(value = "/download-pdf")
    ResponseEntity<byte[]> downloadJasper() throws Exception;

}
