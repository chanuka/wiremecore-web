package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.ResourceResource;
import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.service.ResourceService;
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

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.resources}")
@Tag(name = "Resource Management", description = "Provides Resource Management API's")
public class ResourceController implements ResourceResource {

    private final ResourceService service;
    private final MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Override
    public ResponseEntity<List<ResourceResponseDto>> getAll() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("RESOURCE_GET_ALL_DEBUG", null, currentLocale));
        try {
            List<ResourceResponseDto> responseDtoList = service.findAll();
            return ResponseEntity.ok().body(responseDtoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
