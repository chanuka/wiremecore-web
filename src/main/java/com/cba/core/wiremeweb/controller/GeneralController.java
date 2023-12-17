package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GeneralResource;
import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.service.GeneralService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Tag(name = "General API Management", description = "This is included all general purpose API list")
public class GeneralController implements GeneralResource {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);
    private final MessageSource messageSource;
    private final GeneralService service;

    public ResponseEntity<Map<String, List<DistrictDto>>> getDistricts() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DISTRICT_GET_ALL_DEBUG", null, currentLocale));
        try {
            Map<String, List<DistrictDto>> responseDtoList = service.findAllDistrict();

            return ResponseEntity.ok().body(responseDtoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }
}
