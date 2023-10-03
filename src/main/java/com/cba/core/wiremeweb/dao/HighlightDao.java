package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;

import java.util.List;
import java.util.Map;

public interface HighlightDao {

    List<HighlightResponseDto> findAll(String configType) throws Exception;

    HighlightResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception;

    HighlightResponseDto create(HighlightRequestDto requestDto) throws Exception;

    HighlightResponseDto update(String configName, HighlightRequestDto requestDto) throws Exception;

    Map<Integer, Map<String, Object>> findHighLights(HighlightRequestDto requestDto) throws Exception;


}
