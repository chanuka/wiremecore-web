package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public interface HighlightResource {

    @GetMapping("/getHighlightConfig")
    ResponseEntity<List<HighlightResponseDto>> getHighlightConfig(@RequestParam String configType) throws Exception;

    @DeleteMapping("/deleteHighlightConfig")
    ResponseEntity<HighlightResponseDto> deleteHighlightConfig(@RequestParam String configName) throws Exception;

    @PostMapping("/setHighlightConfig")
    ResponseEntity<HighlightResponseDto> createHighlightConfig(@RequestBody HighlightRequestDto requestDto) throws Exception;

    @PutMapping("/updateHighlightConfig/{configName}")
    ResponseEntity<HighlightResponseDto> updateHighlightConfig(@PathVariable(value = "configName") String configName,
                                                          @RequestBody HighlightRequestDto requestDto) throws Exception;

    @PostMapping("/getHighlights")
    ResponseEntity<Map<String, Map<String, Object>>> getHighlights(@RequestBody HighlightRequestDto requestDto) throws Exception;

    @GetMapping("/getHighlightsDetail")
    ResponseEntity<Map<String, TransactionCoreResponseDto>> getHighlightsDetail(@RequestBody HighlightRequestDto requestDto) throws Exception;

}
