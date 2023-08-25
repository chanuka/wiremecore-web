package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
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
public class MerchantCustomerServiceImpl implements GenericService<MerchantCustomerResponseDto, MerchantCustomerRequestDto> {

    private final GenericDao<MerchantCustomerResponseDto, MerchantCustomerRequestDto> dao;
    private final UserBean userBean;

    @Override
    public Page<MerchantCustomerResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<MerchantCustomerResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<MerchantCustomerResponseDto> findBySearchParamLike(List searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    @Override
    public MerchantCustomerResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public MerchantCustomerResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public MerchantCustomerResponseDto updateById(int id, MerchantCustomerRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public MerchantCustomerResponseDto create(MerchantCustomerRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<MerchantCustomerResponseDto> createBulk(List<MerchantCustomerRequestDto> requestDtoList) throws Exception {
        return dao.createBulk(requestDtoList);
    }

    @Override
    public byte[] exportPdfReport() throws Exception {
        List<MerchantCustomerResponseDto> responseDtoList = dao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/merchantcustomer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(responseDtoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] exportExcelReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        String[] columnHeaders = {"Id", "Name", "Address", "Contact No", "Email", "Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        List<MerchantCustomerResponseDto> responseDtoList = dao.findAll();

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
        return excelBytes;
    }
}
