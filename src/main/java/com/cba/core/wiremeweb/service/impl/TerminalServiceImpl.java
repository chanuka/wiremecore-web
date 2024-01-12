package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.MerchantDao;
import com.cba.core.wiremeweb.dao.TerminalDao;
import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.TerminalMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.MerchantRepository;
import com.cba.core.wiremeweb.service.TerminalService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
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
public class TerminalServiceImpl implements TerminalService<TerminalResponseDto, TerminalRequestDto> {

    private final TerminalDao<Terminal, Terminal> dao;
    private final UserBeanUtil userBeanUtil;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final MerchantDao<Merchant, Merchant> merchantDao;


    @Value("${application.resource.terminals}")
    private String resource;

    @Override
    public Page<TerminalResponseDto> findAll(int page, int pageSize) throws Exception {

        Page<Terminal> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entitiesPage.map(TerminalMapper::toDto);
    }

    @Override
    public List<TerminalResponseDto> findAll() throws Exception {
        List<Terminal> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entityList
                .stream()
                .map(TerminalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TerminalResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Page<Terminal> entitiesPage = dao.findBySearchParamLike(searchParamList, page, pageSize);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entitiesPage.map(TerminalMapper::toDto);
    }

    @Override
    public Page<TerminalResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public TerminalResponseDto findById(int id) throws Exception {
        Terminal entity = dao.findById(id);
        return TerminalMapper.toDto(entity);
    }

    @Override
    public TerminalResponseDto deleteById(int id) throws Exception {
        Terminal entity = dao.findById(id);
        TerminalResponseDto responseDto = TerminalMapper.toDto(entity);

        dao.deleteById(id);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                id, objectMapper.writeValueAsString(responseDto), null,
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        idList.stream()
                .map((id) -> {
                    try {
                        return dao.findById(id);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        dao.deleteByIdList(idList);

        idList.stream()
                .forEach(item -> {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("id", item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                item, objectMapper.writeValueAsString(objectNode), null,
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                    }
                });
    }

    @Override
    public TerminalResponseDto updateById(int id, TerminalRequestDto requestDto) throws Exception {
        Terminal toBeUpdated = dao.findById(id);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getTerminalId().equals(requestDto.getTerminalId())) {
            updateRequired = true;
            oldDataMap.put("terminalId", toBeUpdated.getTerminalId());
            newDataMap.put("terminalId", requestDto.getTerminalId());

            toBeUpdated.setTerminalId(requestDto.getTerminalId());
        }
        if (!toBeUpdated.getMerchant().getMerchantId().equals(requestDto.getMerchantId())) {
            updateRequired = true;
            oldDataMap.put("merchantId", toBeUpdated.getMerchant().getMerchantId());
            newDataMap.put("merchantId", requestDto.getMerchantId());
            Merchant merchant = merchantDao.findByMerchantId(requestDto.getMerchantId());
            toBeUpdated.setMerchant(merchant);
        }
        if (toBeUpdated.getDevice().getId() != requestDto.getDeviceId()) {
            updateRequired = true;
            oldDataMap.put("deviceId", toBeUpdated.getDevice().getId());
            newDataMap.put("deviceId", requestDto.getDeviceId());

            toBeUpdated.setDevice(new Device(requestDto.getDeviceId()));
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(requestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", requestDto.getStatus());

            toBeUpdated.setStatus(new Status(requestDto.getStatus()));
        }
        if (updateRequired) {

            dao.updateById(id, toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return TerminalMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public TerminalResponseDto create(TerminalRequestDto requestDto) throws Exception {
        Merchant merchant = merchantDao.findByMerchantId(requestDto.getMerchantId());

        Terminal toInsert = TerminalMapper.toModel(requestDto, merchant);

        Terminal savedEntity = dao.create(toInsert);
        TerminalResponseDto responseDto = TerminalMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<TerminalResponseDto> createBulk(List<TerminalRequestDto> requestDtoList) throws Exception {
        List<Terminal> entityList = requestDtoList
                .stream()
                .map((terminalDto) -> {
                    Merchant merchant = merchantDao.findByMerchantId(terminalDto.getMerchantId());
                    return TerminalMapper.toModel(terminalDto, merchant);
                })
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    TerminalResponseDto responseDto = TerminalMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
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

        List<TerminalResponseDto> responseDtoList = dao.findAll()
                .stream()
                .map(TerminalMapper::toDto)
                .collect(Collectors.toList()
                );
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
        List<TerminalResponseDto> responseDtoList = dao.findAll()
                .stream()
                .map(TerminalMapper::toDto)
                .collect(Collectors.toList()
                );

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
        Page<Terminal> entitiesPage = dao.findTerminalsByMerchant(id, page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entitiesPage.map(TerminalMapper::toDto);
    }
}
