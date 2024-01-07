package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.MerchantCustomerResource;
import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.service.MerchantService;
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
@RequestMapping("/${application.resource.partners}")
@Tag(name = "Merchant Customer Management", description = "Provides Merchant Customer Management API's")
public class MerchantCustomerController implements MerchantCustomerResource<MerchantCustomerResponseDto, MerchantCustomerRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(MerchantCustomerController.class);

    private final GenericService<MerchantCustomerResponseDto, MerchantCustomerRequestDto> service;
    private final MerchantService<MerchantResponseDto, MerchantRequestDto> merchantService;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<PaginationResponse<MerchantCustomerResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<MerchantCustomerResponseDto> responseDtolist = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<MerchantCustomerResponseDto>(responseDtolist.getContent(), responseDtolist.getTotalElements()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<MerchantCustomerResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_ONE_DEBUG", null, currentLocale));

        try {
            MerchantCustomerResponseDto ResponseDto = service.findById(id);
            return ResponseEntity.ok().body(ResponseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<PaginationResponse<MerchantCustomerResponseDto>> searchAllByPageWise(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<MerchantCustomerResponseDto> responseDtoList = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(new PaginationResponse<MerchantCustomerResponseDto>(responseDtoList.getContent(), responseDtoList.getTotalElements()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            MerchantCustomerResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("MERCHANT_CUSTOMER_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<MerchantCustomerResponseDto> updateOne(int id, MerchantCustomerRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            MerchantCustomerResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<MerchantCustomerResponseDto> createOne(MerchantCustomerRequestDto requestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            MerchantCustomerResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<MerchantCustomerRequestDto> requestDtoList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<MerchantCustomerResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("MERCHANT_CUSTOMER_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("MERCHANT_CUSTOMER_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {

            byte[] excelBytes = service.exportExcelReport();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "Merchant_Customer_List.xlsx");

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
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {

            byte[] pdfBytes = service.exportPdfReport();

            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "Merchant-Customer-details.pdf");
            return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<MerchantResponseDto>> findMerchantsByPartner(int id, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_MERCHANTS_DEBUG", null, currentLocale));

        try {
            Page<MerchantResponseDto> responseDtoList = merchantService.findMerchantsByPartner(id, page, pageSize);
            return ResponseEntity.ok().body(responseDtoList.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<MerchantCustomerResponseDto>> getAllMerchantCustomers() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_ALL_DEBUG", null, currentLocale));
        try {
            List<MerchantCustomerResponseDto> responseDtolist = service.findAll();
            return ResponseEntity.ok().body(responseDtolist);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
