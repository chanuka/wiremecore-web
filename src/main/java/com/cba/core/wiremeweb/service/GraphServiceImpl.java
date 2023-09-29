package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dao.GraphDao;
import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphServiceImpl implements GraphService {

    private final GraphDao dao;

    @Override
    public List<GraphResponseDto> findAll(String configType) throws Exception {
        return dao.findAll(configType);
    }

    @Override
    public GraphResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception {
        return dao.deleteByUser_NameAndConfigType(configName);
    }

    @Override
    public GraphResponseDto create(GraphRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public GraphResponseDto update(String configName, GraphRequestDto requestDto) throws Exception {
        return dao.update(configName, requestDto);
    }
}
