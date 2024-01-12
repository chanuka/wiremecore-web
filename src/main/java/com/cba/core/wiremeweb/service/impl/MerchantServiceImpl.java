package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.MerchantDao;
import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.service.MerchantService;
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
public class MerchantServiceImpl implements MerchantService<MerchantResponseDto, MerchantRequestDto> {

    private final MerchantDao<Merchant, Merchant> dao;
    private final UserBeanUtil userBeanUtil;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.merchants}")
    private String resource;

    @Override
    public Page<MerchantResponseDto> findAll(int page, int pageSize) throws Exception {

        Page<Merchant> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);
    }

    @Override
    public List<MerchantResponseDto> findAll() throws Exception {
        List<Merchant> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entityList
                .stream()
                .map(MerchantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MerchantResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Page<Merchant> entitiesPage = dao.findBySearchParamLike(searchParamList, page, pageSize);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);
    }

    @Override
    public Page<MerchantResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {

        Page<Merchant> entitiesPage = dao.findBySearchParamLikeByKeyWord(searchParameter, page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);
    }

    @Override
    public MerchantResponseDto findById(int id) throws Exception {
        Merchant entity = dao.findById(id);
        return MerchantMapper.toDto(entity);
    }

    @Override
    public MerchantResponseDto deleteById(int id) throws Exception {
        Merchant entity = dao.findById(id);
        MerchantResponseDto responseDto = MerchantMapper.toDto(entity);

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
    public MerchantResponseDto updateById(int id, MerchantRequestDto requestDto) throws Exception {
        Merchant toBeUpdated = dao.findById(id);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getMerchantId().equals(requestDto.getMerchantId())) {
            updateRequired = true;
            oldDataMap.put("merchantId", toBeUpdated.getMerchantId());
            newDataMap.put("merchantId", requestDto.getMerchantId());

            toBeUpdated.setMerchantId(requestDto.getMerchantId());
        }
        if (toBeUpdated.getMerchantCustomer().getId() != requestDto.getPartnerId()) {
            updateRequired = true;
            oldDataMap.put("partnerId", toBeUpdated.getMerchantCustomer().getId());
            newDataMap.put("partnerId", requestDto.getPartnerId());

            toBeUpdated.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
        }
        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getEmail().equals(requestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", requestDto.getEmail());

            toBeUpdated.setEmail(requestDto.getEmail());
        }
        if (!toBeUpdated.getDistrict().equals(requestDto.getDistrict())) {
            updateRequired = true;
            oldDataMap.put("district", toBeUpdated.getDistrict());
            newDataMap.put("district", requestDto.getDistrict());

            toBeUpdated.setDistrict(requestDto.getDistrict());
        }
        if (!toBeUpdated.getProvince().equals(requestDto.getProvince())) {
            updateRequired = true;
            oldDataMap.put("province", toBeUpdated.getProvince());
            newDataMap.put("province", requestDto.getProvince());

            toBeUpdated.setProvince(requestDto.getProvince());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(requestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", requestDto.getStatus());

            toBeUpdated.setStatus(new Status(requestDto.getStatus()));
        }
        if (toBeUpdated.getLat() != requestDto.getLat()) {
            updateRequired = true;
            oldDataMap.put("lat", toBeUpdated.getLat());
            newDataMap.put("lat", requestDto.getLat());

            toBeUpdated.setLat(requestDto.getLat());
        }
        if (toBeUpdated.getLon() != requestDto.getLon()) {
            updateRequired = true;
            oldDataMap.put("lon", toBeUpdated.getLon());
            newDataMap.put("lon", requestDto.getLon());

            toBeUpdated.setLon(requestDto.getLon());
        }
        if (toBeUpdated.getRadius() != requestDto.getRadius()) {
            updateRequired = true;
            oldDataMap.put("lat", toBeUpdated.getRadius());
            newDataMap.put("lat", requestDto.getRadius());

            toBeUpdated.setRadius(requestDto.getRadius());
        }
        if (updateRequired) {

            dao.updateById(id, toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return MerchantMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public MerchantResponseDto create(MerchantRequestDto requestDto) throws Exception {

        Merchant toInsert = MerchantMapper.toModel(requestDto);

        Merchant savedEntity = dao.create(toInsert);
        MerchantResponseDto responseDto = MerchantMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<MerchantResponseDto> createBulk(List<MerchantRequestDto> requestDtoList) throws Exception {
        List<Merchant> entityList = requestDtoList
                .stream()
                .map(MerchantMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    MerchantResponseDto responseDto = MerchantMapper.toDto(item);
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
        List<MerchantResponseDto> responseDtoList = dao.findAll()
                .stream()
                .map(MerchantMapper::toDto)
                .collect(Collectors.toList()
                );
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/merchant.jrxml");
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

        String[] columnHeaders = {"Id", "Merchant ID", "Partner ID", "Name", "Email", "Province", "District", "Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        List<MerchantResponseDto> responseDtoList = dao.findAll()
                .stream()
                .map(MerchantMapper::toDto)
                .collect(Collectors.toList()
                );

        int rowCount = 1;

        for (MerchantResponseDto responseDto : responseDtoList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            Cell cell = row.createCell(columnCount++);
            if (responseDto.getId() instanceof Integer) {
                cell.setCellValue((Integer) responseDto.getId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getMerchantId() instanceof String) {
                cell.setCellValue((String) responseDto.getMerchantId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getPartnerId() instanceof Integer) {
                cell.setCellValue((Integer) responseDto.getPartnerId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getName() instanceof String) {
                cell.setCellValue((String) responseDto.getName());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getEmail() instanceof String) {
                cell.setCellValue((String) responseDto.getEmail());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getProvince() instanceof String) {
                cell.setCellValue((String) responseDto.getProvince());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getDistrict() instanceof String) {
                cell.setCellValue((String) responseDto.getDistrict());
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

    @Override
    public Page<MerchantResponseDto> findMerchantsByPartner(int id, int page, int pageSize) throws Exception {
        Page<Merchant> entitiesPage = dao.findMerchantsByPartner(id, page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entitiesPage.map(MerchantMapper::toDto);

    }
}
