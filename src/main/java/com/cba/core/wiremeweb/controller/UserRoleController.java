package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GenericResource;
import com.cba.core.wiremeweb.dto.UserRoleRequestDto;
import com.cba.core.wiremeweb.dto.UserRoleResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
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
@RequestMapping("/${application.resource.userroles}")
@Tag(name = "User Role Management", description = "Provides User Role Management API's")
public class UserRoleController implements GenericResource<UserRoleResponseDto, UserRoleRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final GenericService<UserRoleResponseDto, UserRoleRequestDto> service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<UserRoleResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("USERROLE_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<UserRoleResponseDto> responseDtolist = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<UserRoleResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("USERROLE_GET_ONE_DEBUG", null, currentLocale));

        try {
            UserRoleResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<UserRoleResponseDto>> searchAllByPageWise(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USERROLE_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            UserRoleResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("USERROLE_DELETE_ONE_SUCCESS", null, currentLocale));

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<UserRoleResponseDto> updateOne(int id, UserRoleRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USERROLE_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            UserRoleResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<UserRoleResponseDto> createOne(UserRoleRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USERROLE_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            UserRoleResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<UserRoleRequestDto> requestDtoList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USERROLE_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<UserRoleResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("USERROLE_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USERROLE_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("USERROLE_DELETE_ALL_SUCCESS", null, currentLocale));
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
