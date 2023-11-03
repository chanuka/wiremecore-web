package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GraphResource;
import com.cba.core.wiremeweb.dto.GraphRequestDto;
import com.cba.core.wiremeweb.dto.GraphResponseDto;
import com.cba.core.wiremeweb.service.GraphService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.users}")
@Tag(name = "Graph Management", description = "Provides Graph Management API's")
public class GraphController implements GraphResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final GraphService service;
    private final MessageSource messageSource;


    @Override
    public ResponseEntity<List<GraphResponseDto>> getAllGraph(String configType) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("GRAPH_CONFIG_GET_ALL_DEBUG", null, currentLocale));

        try {
            List<GraphResponseDto> list = service.findAll(configType);
            return ResponseEntity.ok().body(list);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public ResponseEntity<GraphResponseDto> deleteGraph(String configName) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("GRAPH_DELETE_ONE_DEBUG", null, currentLocale));
        try {
            GraphResponseDto responseDtos = service.deleteByUser_NameAndConfigType(configName);
            return ResponseEntity.ok().body(responseDtos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<GraphResponseDto> createGraph(GraphRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("GRAPH_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            GraphResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<GraphResponseDto> updateGraph(String configName, GraphRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("GRAPH_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            GraphResponseDto responseDto = service.update(configName, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getGraphs(GraphRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("GRAPH_GET_ALL_DEBUG", null, currentLocale));
        try {
            Map<String, Object> responseData = service.findGraphs(requestDto);
            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
