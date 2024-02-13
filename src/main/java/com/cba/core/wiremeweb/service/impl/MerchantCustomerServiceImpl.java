package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantCustomerMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MerchantCustomerServiceImpl implements GenericService<MerchantCustomerResponseDto, MerchantCustomerRequestDto> {

    private final GenericDao<MerchantCustomer> dao;
    private final GlobalAuditDao globalAuditDao;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.partners}")
    private String resource;

    @Override
    public Page<MerchantCustomerResponseDto> findAll(int page, int pageSize) throws Exception {

        Page<MerchantCustomer> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entitiesPage.map(MerchantCustomerMapper::toDto);
    }

    @Override
    public List<MerchantCustomerResponseDto> findAll() throws Exception {
        List<MerchantCustomer> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entityList
                .stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MerchantCustomerResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter) throws Exception {
        List<MerchantCustomer> entityList = dao.findBySearchParamLikeByKeyWord(searchParameter);
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Partners found");
        }
        return entityList
                .stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MerchantCustomerResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Page<MerchantCustomer> entitiesPage = dao.findBySearchParamLike(searchParamList, page, pageSize);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entitiesPage.map(MerchantCustomerMapper::toDto);
    }

    @Override
    public MerchantCustomerResponseDto findById(int id) throws Exception {
        MerchantCustomer entity = dao.findById(id);
        return MerchantCustomerMapper.toDto(entity);
    }

    @Override
    public MerchantCustomerResponseDto deleteById(int id) throws Exception {
        MerchantCustomer entity = dao.findById(id);
        MerchantCustomerResponseDto responseDto = MerchantCustomerMapper.toDto(entity);

        dao.deleteById(id);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                id, objectMapper.writeValueAsString(responseDto), null,
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {

        idList.stream().forEach((id) -> {
            try {
                dao.findById(id);
            } catch (Exception exception) {
                throw new NotFoundException(exception.getMessage());
            }
        });

        dao.deleteByIdList(idList);

        idList.stream()
                .forEach(item -> {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("id", item);
                    try {
                        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                item, objectMapper.writeValueAsString(objectNode), null,
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                    }
                });
    }

    @Override
    public MerchantCustomerResponseDto updateById(int id, MerchantCustomerRequestDto requestDto) throws Exception {
        MerchantCustomer toBeUpdated = dao.findById(id);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getAddress().equals(requestDto.getAddress())) {
            updateRequired = true;
            oldDataMap.put("address", toBeUpdated.getAddress());
            newDataMap.put("address", requestDto.getAddress());

            toBeUpdated.setAddress(requestDto.getAddress());
        }
        if (!toBeUpdated.getContactNo().equals(requestDto.getContactNo())) {
            updateRequired = true;
            oldDataMap.put("contactNo", toBeUpdated.getContactNo());
            newDataMap.put("contactNo", requestDto.getContactNo());

            toBeUpdated.setContactNo(requestDto.getContactNo());
        }
        if (!toBeUpdated.getEmail().equals(requestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", requestDto.getEmail());

            toBeUpdated.setEmail(requestDto.getEmail());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(requestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", requestDto.getStatus());

            toBeUpdated.setStatus(new Status(requestDto.getStatus()));
        }
        if (updateRequired) {

            dao.updateById(id, toBeUpdated);
            globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return MerchantCustomerMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public MerchantCustomerResponseDto create(MerchantCustomerRequestDto requestDto) throws Exception {
        MerchantCustomer toInsert = MerchantCustomerMapper.toModel(requestDto);

        MerchantCustomer savedEntity = dao.create(toInsert);
        MerchantCustomerResponseDto responseDto = MerchantCustomerMapper.toDto(savedEntity);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<MerchantCustomerResponseDto> createBulk(List<MerchantCustomerRequestDto> requestDtoList) throws Exception {
        List<MerchantCustomer> entityList = requestDtoList
                .stream()
                .map(MerchantCustomerMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    MerchantCustomerResponseDto responseDto = MerchantCustomerMapper.toDto(item);
                    try {
                        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportPdfReport() throws Exception {
        List<MerchantCustomerResponseDto> responseDtoList = dao.findAll().stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList()
                );
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/merchantcustomer.jrxml");
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

        String[] columnHeaders = {"Id", "Name", "Address", "Contact No", "Email", "Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        List<MerchantCustomerResponseDto> responseDtoList = dao.findAll().stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList()
                );

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
