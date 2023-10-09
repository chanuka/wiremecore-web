package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.HighlightDao;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HighlightServiceImpl implements HighlightService {

    private final HighlightDao dao;

    @Override
    public List<HighlightResponseDto> findAll(String configType) throws Exception {
        return dao.findAll(configType);
    }

    @Override
    public HighlightResponseDto deleteByUser_NameAndConfigType(String configName) throws Exception {
        return dao.deleteByUser_NameAndConfigType(configName);
    }

    @Override
    public HighlightResponseDto create(HighlightRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public HighlightResponseDto update(String configName, HighlightRequestDto requestDto) throws Exception {
        return dao.update(configName, requestDto);
    }

    @Override
    public Map<String, Map<String, Object>> findHighLights(HighlightRequestDto requestDto) throws Exception {
        return dao.findHighLights(requestDto);
    }

    @Override
    public Map<String, TransactionCoreResponseDto> findHighLightsDetail(HighlightRequestDto requestDto) throws Exception {
        return dao.findHighLightsDetail(requestDto);
    }
}
