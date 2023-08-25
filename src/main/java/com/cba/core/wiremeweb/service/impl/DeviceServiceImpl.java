package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBean;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements GenericService<DeviceResponseDto, DeviceRequestDto> {

    private final GenericDao<DeviceResponseDto, DeviceRequestDto> dao;
    private final UserBean userBean;


    @Override
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<DeviceResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<DeviceResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    public DeviceResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public DeviceResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public DeviceResponseDto updateById(int id, DeviceRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> requestDtolist) throws Exception {
        return dao.createBulk(requestDtolist);
    }

    @Override
    public byte[] exportPdfReport() throws Exception {
        List<DeviceResponseDto> ResponseDtoList = dao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/device.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ResponseDtoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] exportExcelReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        String[] columnHeaders = {"Id", "Serial", "IMEI", "Device Type", "Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        List<DeviceResponseDto> responseDtoist = dao.findAll();

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
        return excelBytes;
    }
}
