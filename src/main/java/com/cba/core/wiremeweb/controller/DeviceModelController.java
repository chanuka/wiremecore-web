package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GenericResource;
import com.cba.core.wiremeweb.dto.DeviceModelRequestDto;
import com.cba.core.wiremeweb.dto.DeviceModelResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.device_model}")
@Tag(name = "Device Model Management", description = "Provides Device Model Management API's")
public class DeviceModelController implements GenericResource<DeviceModelResponseDto, DeviceModelRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final GenericService<DeviceModelResponseDto, DeviceModelRequestDto> service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<PaginationResponse<DeviceModelResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICEMODEL_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<DeviceModelResponseDto> responseDtoList = service.findAll(page, pageSize);

            return ResponseEntity.ok().body(new PaginationResponse<>(responseDtoList.getContent(), responseDtoList.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceModelResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICEMODEL_GET_ONE_DEBUG", null, currentLocale));

        try {
            DeviceModelResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<PaginationResponse<DeviceModelResponseDto>> searchAllByPageWise(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEMODEL_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<DeviceModelResponseDto> responseDtolist = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<DeviceModelResponseDto>(responseDtolist.getContent(), responseDtolist.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEMODEL_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            DeviceModelResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICEMODEL_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceModelResponseDto> updateOne(int id, DeviceModelRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEMODEL_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceModelResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceModelResponseDto> createOne(DeviceModelRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEMODEL_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceModelResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<DeviceModelRequestDto> requestDtoList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEMODEL_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<DeviceModelResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICEMODEL_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEMODEL_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICEMODEL_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadJasper() throws Exception {
        return null;
    }
}
