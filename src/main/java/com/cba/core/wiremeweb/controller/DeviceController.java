package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.DeviceResource;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.InternalServerError;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.service.GlobalAuditEntryService;
import com.cba.core.wiremeweb.service.impl.DeviceServiceImpl;
import com.cba.core.wiremeweb.util.UpdateResponse;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Validated
public class DeviceController implements DeviceResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceServiceImpl deviceServiceImpl;
    private final GlobalAuditEntryService globalAuditEntryService;

    @Value("${application.resource.devices}")
    private String resource;

    @Override
    public ResponseEntity<List<DeviceResponseDto>> devices(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int pageSize) throws Exception {

        logger.debug("GET Devices List is called");
        try {
            Page<DeviceResponseDto> list = deviceServiceImpl.findAll(page, pageSize);
            return ResponseEntity.ok().body(list.getContent());
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred");
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> getADevice(@PathVariable(value = "id") int id) throws Exception {
        logger.debug("GET Single Device is called");

        DeviceResponseDto device = null;
        try {
            device = deviceServiceImpl.findById(id);

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred");
        }
        return ResponseEntity.ok().body(device);
    }

    @Override
    public ResponseEntity<List<DeviceResponseDto>> searchDevices(@RequestParam(value = "serialNumber") String serialNumber,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int pageSize) throws Exception {
        logger.debug("GET Search Devices is called");

        try {
            Page<DeviceResponseDto> deviceList = deviceServiceImpl.findBySerialNoLike(serialNumber, page, pageSize);
            return ResponseEntity.ok().body(deviceList.getContent());
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred");
        }

    }

    @Override
    public ResponseEntity<String> deleteADevice(@PathVariable(value = "id") int id) throws Exception {

        logger.debug("Delete Single Device is called");

        try {

            deviceServiceImpl.deleteById(id);
            return ResponseEntity.ok().body("Success");

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (RecordInUseException ru) {
            logger.error(ru.getMessage());
            throw ru;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred : Device cannot be deleted");
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> updateADevice(@PathVariable(value = "id") int id,
                                                           @RequestBody DeviceRequestDto deviceRequestDto) throws Exception {
        logger.debug("Update Single Device is called");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UpdateResponse<DeviceResponseDto> response = deviceServiceImpl.updateById(id, deviceRequestDto);
            globalAuditEntryService.createNewRevision(
                    resource,
                    id,
                    UserOperationEnum.UPDATE.getValue(),
                    objectMapper.writeValueAsString(response.getOldDataMap()),
                    objectMapper.writeValueAsString(response.getNewDataMap()));

            return ResponseEntity.ok().body(response.getT());

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred");
        }
    }

    @Override
    public ResponseEntity<DeviceResponseDto> createADevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) throws Exception {

        logger.debug("Create Single Device is called");

        try {

            DeviceResponseDto device = deviceServiceImpl.create(deviceRequestDto);
            return ResponseEntity.ok().body(device);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred");
        }
    }

    @Override
    public ResponseEntity<String> createDevices(@RequestBody List<DeviceRequestDto> list) throws Exception {

        logger.debug("Create Bulk Devices is called");

        try {
            deviceServiceImpl.createBulk(list);
            return ResponseEntity.ok().body("Devices are created successfully");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred");
        }
    }

    @Override
    public ResponseEntity<String> deleteDevices(@RequestBody List<Map<String, Integer>> deviceIdList) throws Exception {

        logger.debug("Delete Bulk Devices is called");
        try {
            deviceServiceImpl.deleteByIdList(deviceIdList);
            return ResponseEntity.ok().body("Devices are deleted successfully");
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (RecordInUseException ru) {
            logger.error(ru.getMessage());
            throw ru;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred : Device cannot be deleted");
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        logger.debug("Generate Excel for Devices List is called");

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");

            String[] columnHeaders = {"Id", "Serial", "IMEI", "Device Type", "Status"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            List<DeviceResponseDto> list = deviceServiceImpl.findAll();

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
            throw new InternalServerError("Internal Server Error has Occurred : Excel for the Device list cannot be generated");
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadJasper() throws Exception {

        try {
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "device-details.pdf");
            return new ResponseEntity<byte[]>(deviceServiceImpl.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError("Internal Server Error has Occurred : PDF for the Device list cannot be generated");
        }

    }

}
