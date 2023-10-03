package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.HighlightResource;
import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import com.cba.core.wiremeweb.service.HighlightService;
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
public class HighlightController implements HighlightResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final HighlightService service;
    private final MessageSource messageSource;


    @Override
    public ResponseEntity<List<HighlightResponseDto>> getHighlightConfig(String configType) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("HIGHLIGHTS_CONFIG_GET_ALL_DEBUG", null, currentLocale));

        try {
            List<HighlightResponseDto> list = service.findAll(configType);
            return ResponseEntity.ok().body(list);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public ResponseEntity<HighlightResponseDto> deleteHighlightConfig(String configName) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("HIGHLIGHTS_DELETE_ONE_DEBUG", null, currentLocale));
        try {
            HighlightResponseDto responseDtos = service.deleteByUser_NameAndConfigType(configName);
            return ResponseEntity.ok().body(responseDtos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<HighlightResponseDto> createHighlightConfig(HighlightRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("HIGHLIGHTS_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            HighlightResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<HighlightResponseDto> updateHighlightConfig(String configName, HighlightRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("HIGHLIGHTS_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            HighlightResponseDto responseDto = service.update(configName, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Object>>> getHighlights(HighlightRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("HIGHLIGHTS_GET_ALL_DEBUG", null, currentLocale));
        try {
            Map<String, Map<String, Object>> responseData = service.findHighLights(requestDto);
            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
