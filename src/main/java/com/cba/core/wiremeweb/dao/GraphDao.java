package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;

import java.util.List;
import java.util.Map;

public interface GraphDao {

    List<GraphResponseDto> findAll(String configType) throws Exception;

    GraphResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception;

    GraphResponseDto create(GraphRequestDto requestDto) throws Exception;

    GraphResponseDto update(String configName, GraphRequestDto requestDto) throws Exception;

    Map<String, Object> findGraphs(GraphRequestDto requestDto) throws Exception;

}
