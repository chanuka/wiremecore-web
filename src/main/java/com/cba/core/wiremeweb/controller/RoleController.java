package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GenericResource;
import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
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
@RequestMapping("/${application.resource.roles}")
public class RoleController implements GenericResource<RoleResponseDto, RoleRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private final GenericService<RoleResponseDto, RoleRequestDto> service;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<RoleResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("ROLE_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<RoleResponseDto> responseDtolist = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<RoleResponseDto> getOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("ROLE_GET_ONE_DEBUG", null, currentLocale));

        try {
            RoleResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<RoleResponseDto>> searchAllByPageWise(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<RoleResponseDto> responseDtolist = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            RoleResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("ROLE_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<RoleResponseDto> updateOne(int id, RoleRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            RoleResponseDto response = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<RoleResponseDto> createOne(RoleRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            RoleResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<RoleRequestDto> requestDtoList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<RoleResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("ROLE_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("ROLE_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");

            String[] columnHeaders = {"Id", "Role Name", "Status"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            List<RoleResponseDto> responseDtoList = service.findAll();

            int rowCount = 1;

            for (RoleResponseDto responseDto : responseDtoList) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell cell = row.createCell(columnCount++);
                if (responseDto.getId() instanceof Integer) {
                    cell.setCellValue((Integer) responseDto.getId());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getRoleName() instanceof String) {
                    cell.setCellValue((String) responseDto.getRoleName());
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
            httpHeaders.setContentDispositionFormData("attachment", "Role_List.xlsx");

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
        logger.debug(messageSource.getMessage("ROLE_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "role-details.pdf");
            return new ResponseEntity<byte[]>(service.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
