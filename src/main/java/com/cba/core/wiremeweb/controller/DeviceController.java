package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.DeviceResource;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.InternalServerError;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.service.DeviceService;
import jakarta.validation.Valid;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Validated
public class DeviceController implements DeviceResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;
    private final MessageSource messageSource;


    @Override
    public ResponseEntity<List<DeviceResponseDto>> devices(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICE_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<DeviceResponseDto> list = deviceService.findAll(page, pageSize);
            return ResponseEntity.ok().body(list.getContent());
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> getADevice(@PathVariable(value = "id") int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("DEVICE_GET_ONE_DEBUG", null, currentLocale));

        DeviceResponseDto device = null;
        try {
            device = deviceService.findById(id);

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
        return ResponseEntity.ok().body(device);
    }

    @Override
    public ResponseEntity<List<DeviceResponseDto>> searchDevices(@RequestParam(value = "serialNumber") String serialNumber,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<DeviceResponseDto> deviceList = deviceService.findBySerialNoLike(serialNumber, page, pageSize);
            return ResponseEntity.ok().body(deviceList.getContent());
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }

    }

    @Override
    public ResponseEntity<String> deleteADevice(@PathVariable(value = "id") int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_DELETE_ONE_DEBUG", null, currentLocale));

        try {

            DeviceResponseDto deviceResponseDto = deviceService.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICE_DELETE_ONE_SUCCESS", null, currentLocale));

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (RecordInUseException ru) {
            logger.error(ru.getMessage());
            throw ru;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> updateADevice(@PathVariable(value = "id") int id,
                                                           @RequestBody DeviceRequestDto deviceRequestDto) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            DeviceResponseDto response = deviceService.updateById(id, deviceRequestDto);
            return ResponseEntity.ok().body(response);

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> createADevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_CREATE_ONE_DEBUG", null, currentLocale));
        try {

            DeviceResponseDto device = deviceService.create(deviceRequestDto);
            return ResponseEntity.ok().body(device);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> createDevices(@RequestBody List<DeviceRequestDto> list) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<DeviceResponseDto> deviceList = deviceService.createBulk(list);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICE_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> deleteDevices(@RequestBody List<Integer> deviceIdList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("DEVICE_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            deviceService.deleteByIdList(deviceIdList);
            return ResponseEntity.ok().body(messageSource.getMessage("DEVICE_DELETE_ALL_SUCCESS", null, currentLocale));

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (RecordInUseException ru) {
            logger.error(ru.getMessage());
            throw ru;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
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
            List<DeviceResponseDto> list = deviceService.findAll();

            int rowCount = 1;

            for (DeviceResponseDto device : list) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell cell = row.createCell(columnCount++);
                if (device.getId() instanceof Integer) {
                    cell.setCellValue((Integer) device.getId());
                }
                cell = row.createCell(columnCount++);
                if (device.getSerialNo() instanceof String) {
                    cell.setCellValue((String) device.getSerialNo());
                }
                cell = row.createCell(columnCount++);
                if (device.getEmiNo() instanceof String) {
                    cell.setCellValue((String) device.getEmiNo());
                }
                cell = row.createCell(columnCount++);
                if (device.getDeviceType() instanceof String) {
                    cell.setCellValue((String) device.getDeviceType());
                }
                cell = row.createCell(columnCount++);
                if (device.isActive()) {
                    cell.setCellValue((String) "Active");
                } else {
                    cell.setCellValue((String) "Inactive");
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
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
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
            return new ResponseEntity<byte[]>(deviceService.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }

    }

}
