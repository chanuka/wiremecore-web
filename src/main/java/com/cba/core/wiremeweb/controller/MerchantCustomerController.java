package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GenericResource;
import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.partners}")
public class MerchantCustomerController implements GenericResource<MerchantCustomerResponseDto, MerchantCustomerRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(MerchantCustomerController.class);

    private final GenericService<MerchantCustomerResponseDto, MerchantCustomerRequestDto> service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<MerchantCustomerResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<MerchantCustomerResponseDto> responseDtolist = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
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
    public ResponseEntity<List<MerchantCustomerResponseDto>> searchAllByPageWise(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<MerchantCustomerResponseDto> responseDtolist = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
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
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");

            String[] columnHeaders = {"Id", "Name", "Address", "Contact No", "Email", "Status"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            List<MerchantCustomerResponseDto> responseDtoList = service.findAll();

            int rowCount = 1;

            for (MerchantCustomerResponseDto responseDto : responseDtoList) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell cell = row.createCell(columnCount++);
                if (responseDto.getId() instanceof Integer) {
                    cell.setCellValue((Integer) responseDto.getId());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getName() instanceof String) {
                    cell.setCellValue((String) responseDto.getName());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getAddress() instanceof String) {
                    cell.setCellValue((String) responseDto.getAddress());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getContactNo() instanceof String) {
                    cell.setCellValue((String) responseDto.getContactNo());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getEmail() instanceof String) {
                    cell.setCellValue((String) responseDto.getEmail());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getStatus() instanceof String) {
                    cell.setCellValue((String) responseDto.getStatus());
                }
            }

            byte[] excelBytes;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                excelBytes = outputStream.toByteArray();
            }

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
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "Merchant-Customer-details.pdf");
            return new ResponseEntity<byte[]>(service.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
