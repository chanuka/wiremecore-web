package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.TerminalDao;
import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.service.TerminalService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
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
public class TerminalServiceImpl implements TerminalService<TerminalResponseDto, TerminalRequestDto> {

    private final TerminalDao<TerminalResponseDto, TerminalRequestDto> dao;
    private final UserBeanUtil userBeanUtil;

    @Override
    public Page<TerminalResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<TerminalResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<TerminalResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    @Override
    public TerminalResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public TerminalResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);

    }

    @Override
    public TerminalResponseDto updateById(int id, TerminalRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public TerminalResponseDto create(TerminalRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<TerminalResponseDto> createBulk(List<TerminalRequestDto> requestDtoList) throws Exception {
        return dao.createBulk(requestDtoList);
    }

    @Override
    public byte[] exportPdfReport() throws Exception {

        List<TerminalResponseDto> responseDtoList = dao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/terminal.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(responseDtoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBeanUtil.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] exportExcelReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        String[] columnHeaders = {"Id", "Terminal Id", "Merchant Id", "Device Id", "Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        List<TerminalResponseDto> responseDtoList = dao.findAll();

        int rowCount = 1;

        for (TerminalResponseDto responseDto : responseDtoList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            Cell cell = row.createCell(columnCount++);
            if (responseDto.getId() instanceof Integer) {
                cell.setCellValue((Integer) responseDto.getId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getTerminalId() instanceof String) {
                cell.setCellValue((String) responseDto.getTerminalId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getStatus() instanceof String) {
                cell.setCellValue((String) responseDto.getStatus());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getMerchantId() instanceof String) {
                cell.setCellValue((String) responseDto.getMerchantId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getDeviceId() instanceof Integer) {
                cell.setCellValue((Integer) responseDto.getDeviceId());
            }
        }

        byte[] excelBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            excelBytes = outputStream.toByteArray();
        }
        return excelBytes;
    }

    @Override
    public Page<TerminalResponseDto> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception {
        return dao.findTerminalsByMerchant(id, page, pageSize);
    }
}
