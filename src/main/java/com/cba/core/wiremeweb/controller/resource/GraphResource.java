package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public interface GraphResource {

    @GetMapping("/getGraphConfig")
    ResponseEntity<List<GraphResponseDto>> getAllGraph(@RequestParam String configType) throws Exception;

    @DeleteMapping("/getGraphConfig")
    ResponseEntity<GraphResponseDto> deleteGraph(@RequestParam String configName) throws Exception;

    @PostMapping("/setGraphConfig")
    ResponseEntity<GraphResponseDto> createGraph(@RequestBody GraphRequestDto requestDto) throws Exception;

    @PutMapping("/setGraphConfig/{configName}")
    ResponseEntity<GraphResponseDto> updateGraph(@PathVariable(value = "configName") String configName,
                                                      @RequestBody GraphRequestDto requestDto) throws Exception;

    @PostMapping("/getGraphs")
    ResponseEntity<Map<String, Object>> getGraphs(@RequestBody GraphRequestDto requestDto) throws Exception;


}
