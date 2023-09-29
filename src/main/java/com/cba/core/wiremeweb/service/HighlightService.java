package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;

import java.util.List;

public interface HighlightService {

    List<HighlightResponseDto> findAll(String configType) throws Exception;

    HighlightResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception;

    HighlightResponseDto create(HighlightRequestDto requestDto) throws Exception;

    HighlightResponseDto update(String configName, HighlightRequestDto requestDto) throws Exception;

}
