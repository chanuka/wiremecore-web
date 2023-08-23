package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.GenericResource;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
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
@RequestMapping("/${application.resource.devices}")
public class DeviceController implements GenericResource<DeviceResponseDto, DeviceRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final GenericService<DeviceResponseDto, DeviceRequestDto> service;
    private final MessageSource messageSource;


    @Override
    public ResponseEntity<List<DeviceResponseDto>> getAllByPageWise(int page, int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICE_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<DeviceResponseDto> responseDtolist = service.findAll(page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> getOne(int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICE_GET_ONE_DEBUG", null, currentLocale));

        try {
            DeviceResponseDto responseDto = service.findById(id);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<DeviceResponseDto>> searchAllByPageWise(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<DeviceResponseDto> responseDtolist = service.findBySearchParamLike(searchParamList, page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public ResponseEntity<String> deleteOne(int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_DELETE_ONE_DEBUG", null, currentLocale));

        try {
            DeviceResponseDto responseDto = service.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICE_DELETE_ONE_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> updateOne(int id, DeviceRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceResponseDto responseDto = service.updateById(id, requestDto);
            return ResponseEntity.ok().body(responseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> createOne(DeviceRequestDto requestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_CREATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceResponseDto responseDto = service.create(requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> createBulk(List<DeviceRequestDto> requestDtoList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<DeviceResponseDto> responseDtoList = service.createBulk(requestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICE_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<String> deleteBulkByIdList(List<Integer> idList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            service.deleteByIdList(idList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICE_DELETE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");

            String[] columnHeaders = {"Id", "Serial", "IMEI", "Device Type", "Status"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            List<DeviceResponseDto> responseDtoist = service.findAll();

            int rowCount = 1;

            for (DeviceResponseDto responseDto : responseDtoist) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell cell = row.createCell(columnCount++);
                if (responseDto.getId() instanceof Integer) {
                    cell.setCellValue((Integer) responseDto.getId());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getSerialNo() instanceof String) {
                    cell.setCellValue((String) responseDto.getSerialNo());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getEmiNo() instanceof String) {
                    cell.setCellValue((String) responseDto.getEmiNo());
                }
                cell = row.createCell(columnCount++);
                if (responseDto.getDeviceType() instanceof String) {
                    cell.setCellValue((String) responseDto.getDeviceType());
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
            httpHeaders.setContentDispositionFormData("attachment", "Device_List.xlsx");

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
        logger.debug(messageSource.getMessage("DEVICE_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "device-details.pdf");
            return new ResponseEntity<byte[]>(service.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

}
