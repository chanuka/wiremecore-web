package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.UserResource;
import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.users}")
public class UserController implements UserResource<UserResponseDto, UserRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService<UserResponseDto, UserRequestDto> service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<UserResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("USER_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<UserResponseDto> responseDtolist = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<UserResponseDto> getOne(int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("USER_GET_ONE_DEBUG", null, currentLocale));

        try {
            UserResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> searchAllByPageWise(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<UserResponseDto> responseDtolist = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            UserResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_DELETE_ONE_SUCCESS", null, currentLocale));

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<UserResponseDto> updateOne(int id, UserRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            UserResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<UserResponseDto> createOne(UserRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            UserResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<UserRequestDto> requestDtoList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<UserResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {

            byte[] excelBytes = service.exportExcelReport();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "User_List.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(httpHeaders)
                    .body(excelBytes);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadJasper() throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {

            byte[] pdfBytes = service.exportPdfReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "user-details.pdf");

            return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_CHANGE_PASSWORD_DEBUG", null, currentLocale));

        try {
            service.changePassword(requestDto);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_CHANGE_PASSWORD_SUCCESS", null, currentLocale));

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
