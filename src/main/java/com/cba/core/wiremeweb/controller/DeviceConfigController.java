package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.DeviceConfigResource;
import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import com.cba.core.wiremeweb.service.DeviceConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.deviceconfig}")
@Tag(name = "Device Config Management", description = "Provides Device Config Management API's")
public class DeviceConfigController implements DeviceConfigResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceConfigService service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<DeviceConfigResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICECONFIG_GET_ONE_DEBUG", null, currentLocale));

        try {
            DeviceConfigResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceConfigResponseDto> createOne(DeviceConfigRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICECONFIG_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceConfigResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceConfigResponseDto> updateOne(DeviceConfigRequestDto requestDto, int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICECONFIG_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceConfigResponseDto responseDto = service.update(id, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICECONFIG_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            DeviceConfigResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICECONFIG_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
