package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public interface HighlightResource {

    @GetMapping("/getHighlightConfig")
    ResponseEntity<List<HighlightResponseDto>> getAllHighlights(@RequestParam String configType) throws Exception;

    @DeleteMapping("/getHighlightConfig")
    ResponseEntity<HighlightResponseDto> deleteHighlights(@RequestParam String configName) throws Exception;

    @PostMapping("/setHighlightConfig")
    ResponseEntity<HighlightResponseDto> createHighlights(@RequestBody HighlightRequestDto requestDto) throws Exception;

    @PutMapping("/setHighlightConfig/{configName}")
    ResponseEntity<HighlightResponseDto> updateHighlights(@PathVariable(value = "configName") String configName,
                                                          @RequestBody HighlightRequestDto requestDto) throws Exception;

}
