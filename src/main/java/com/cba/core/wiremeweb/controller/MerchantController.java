package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.MerchantResource;
import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.service.TerminalService;
import com.cba.core.wiremeweb.util.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/${application.resource.merchants}")
@Tag(name = "Merchant Management", description = "Provides Merchant Management API's")
public class MerchantController implements MerchantResource<MerchantResponseDto, MerchantRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    private final GenericService<MerchantResponseDto, MerchantRequestDto> service;
    private final TerminalService<TerminalResponseDto, TerminalRequestDto> terminalService;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<PaginationResponse<MerchantResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<MerchantResponseDto> responseDtoList = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<>(responseDtoList.getContent(), responseDtoList.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<MerchantResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_GET_ONE_DEBUG", null, currentLocale));

        try {
            MerchantResponseDto ResponseDto = service.findById(id);
            return ResponseEntity.ok().body(ResponseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<PaginationResponse<MerchantResponseDto>> searchAllByPageWise(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<MerchantResponseDto> responseDtoList = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<>(responseDtoList.getContent(), responseDtoList.getTotalElements()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("MERCHANT_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<MerchantResponseDto> updateOne(int id, MerchantRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            MerchantResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<MerchantResponseDto> createOne(MerchantRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            MerchantResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<MerchantRequestDto> requestDtoList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("MERCHANT_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("MERCHANT_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {

            byte[] excelBytes = service.exportExcelReport();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "Merchant_List.xlsx");

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
        logger.debug(messageSource.getMessage("MERCHANT_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {

            byte[] pdfBytes = service.exportPdfReport();

            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "Merchant-details.pdf");
            return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<TerminalResponseDto>> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_MERCHANTS_DEBUG", null, currentLocale));

        try {
            Page<TerminalResponseDto> responseDtoList = terminalService.findTerminalsByMerchant(id, page, pageSize);
            return ResponseEntity.ok().body(responseDtoList.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<MerchantResponseDto>> getAllMerchants() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_GET_ALL_DEBUG", null, currentLocale));
        try {
            List<MerchantResponseDto> responseDtoList = service.findAll();
            return ResponseEntity.ok().body(responseDtoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<MerchantResponseDto>> searchAllByPageWiseByKey(Map<String, String> searchParameter) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            List<MerchantResponseDto> responseDtoList = service.findBySearchParamLikeByKeyWord(searchParameter);
            return ResponseEntity.ok().body(responseDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        }
    }
}
