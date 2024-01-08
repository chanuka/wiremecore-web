package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GenericResource;
import com.cba.core.wiremeweb.dto.DeviceVendorRequestDto;
import com.cba.core.wiremeweb.dto.DeviceVendorResponseDto;
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
@RequestMapping("/${application.resource.device_vendor}")
@Tag(name = "Device Vendor Management", description = "Provides Device Vendor Management API's")
public class DeviceVendorController implements GenericResource<DeviceVendorResponseDto, DeviceVendorRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final GenericService<DeviceVendorResponseDto, DeviceVendorRequestDto> service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<PaginationResponse<DeviceVendorResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICEVENDOR_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<DeviceVendorResponseDto> responseDtoList = service.findAll(page, pageSize);

            return ResponseEntity.ok().body(new PaginationResponse<DeviceVendorResponseDto>(responseDtoList.getContent(), responseDtoList.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceVendorResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICEVENDOR_GET_ONE_DEBUG", null, currentLocale));

        try {
            DeviceVendorResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<PaginationResponse<DeviceVendorResponseDto>> searchAllByPageWise(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEVENDOR_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<DeviceVendorResponseDto> responseDtolist = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<>(responseDtolist.getContent(), responseDtolist.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEVENDOR_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            DeviceVendorResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICEVENDOR_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceVendorResponseDto> updateOne(int id, DeviceVendorRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEVENDOR_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceVendorResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceVendorResponseDto> createOne(DeviceVendorRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEVENDOR_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceVendorResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<DeviceVendorRequestDto> requestDtoList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEVENDOR_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<DeviceVendorResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICEVENDOR_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICEVENDOR_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICEVENDOR_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadJasper() throws Exception {
        return null;
    }
}
