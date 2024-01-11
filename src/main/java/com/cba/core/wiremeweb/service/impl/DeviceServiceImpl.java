package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.DeviceDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.dto.DistributionResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.service.DeviceService;
import com.cba.core.wiremeweb.util.PaginationResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService<DeviceResponseDto, DeviceRequestDto> {

    private final DeviceDao<Device, Device> dao;
    private final UserBeanUtil userBeanUtil;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.devices}")
    private String resource;


    @Override
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {

        Page<Device> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceMapper::toDto);
    }

    @Override
    public List<DeviceResponseDto> findAll() throws Exception {
        List<Device> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entityList
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {

        Page<Device> entitiesPage = dao.findBySearchParamLike(searchParamList, page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceMapper::toDto);
    }

    @Override
    public Page<DeviceResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    public DeviceResponseDto findById(int id) throws Exception {
        return DeviceMapper.toDto(dao.findById(id));
    }

    @Override
    public DeviceResponseDto deleteById(int id) throws Exception {

        Device entity = dao.findById(id);
        DeviceResponseDto responseDto = DeviceMapper.toDto(entity);
        dao.deleteById(id);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                id, objectMapper.writeValueAsString(responseDto), null,
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
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
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    @Override
    public DeviceResponseDto updateById(int id, DeviceRequestDto requestDto) throws Exception {

        Device toBeUpdated = dao.findById(id);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getDeviceType().equals(requestDto.getDeviceType())) {
            updateRequired = true;
            oldDataMap.put("deviceType", toBeUpdated.getDeviceType());
            newDataMap.put("deviceType", requestDto.getDeviceType());

            toBeUpdated.setDeviceType(requestDto.getDeviceType());
        }
        if (!toBeUpdated.getEmiNo().equals(requestDto.getEmiNo())) {
            updateRequired = true;
            oldDataMap.put("emiNo", toBeUpdated.getEmiNo());
            newDataMap.put("emiNo", requestDto.getEmiNo());

            toBeUpdated.setEmiNo(requestDto.getEmiNo());
        }
        if (!toBeUpdated.getSerialNo().equals(requestDto.getSerialNo())) {
            updateRequired = true;
            oldDataMap.put("serialNo", toBeUpdated.getSerialNo());
            newDataMap.put("serialNo", requestDto.getSerialNo());

            toBeUpdated.setSerialNo(requestDto.getSerialNo());
        }
        if ((toBeUpdated.getDeviceModel() != null ? toBeUpdated.getDeviceModel().getId() : null) != requestDto.getModelId()) {
            updateRequired = true;
            oldDataMap.put("modelId", (toBeUpdated.getDeviceModel() != null ? toBeUpdated.getDeviceModel().getId() : null));
            newDataMap.put("modelId", requestDto.getModelId());

            toBeUpdated.setDeviceModel(new DeviceModel(requestDto.getModelId()));
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

            return DeviceMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }

    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto requestDto) throws Exception {

        Device toInsert = DeviceMapper.toModel(requestDto);
        toInsert.setStatus(new Status(requestDto.getStatus()));
        Device savedEntity = dao.create(toInsert);
        DeviceResponseDto responseDto = DeviceMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> requestDtolist) throws Exception {

        List<Device> entityList = requestDtolist
                .stream()
                .map(DeviceMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    DeviceResponseDto responseDto = DeviceMapper.toDto(item);
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
        List<DeviceResponseDto> responseDtoist = dao.findAll()
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList()
                );
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/device.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(responseDtoist);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBeanUtil.getUsername()); // username can be extracted once the url is accessible

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
        List<DeviceResponseDto> responseDtoist = dao.findAll()
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList()
                );

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

    @Override
    public List<DistributionResponseDto> getDeviceDistribution(Map<String, String> grouping) throws Exception {

        List<DistributionResponseDto> responseData = new ArrayList<>();
        String groupingValue = grouping.get("grouping");

        if (groupingValue != null && ("Vendor".equals(groupingValue) || "DeviceModel".equals(groupingValue))) {

            String selectClause = setSelectCondition(groupingValue);
            String groupByClause = setGroupByCondition(groupingValue);

            List<Object[]> list = dao.getDeviceDistribution(selectClause, groupByClause);
            extracted(responseData, list);
        }
        return responseData;
    }

    @Override
    public PaginationResponse<DeviceResponseDto> getGeoFenceDevice(Map<String, Object> filter) throws Exception {

        Map<String, Boolean> filterValue = (Map<String, Boolean>) filter.get("filter");
        Boolean isAway = filterValue.get("isAway");

        List<Device> entityList = dao.getGeoFenceDevice(isAway);

        return new PaginationResponse<>(entityList
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList()), entityList.size());
    }

    private String setSelectCondition(String groupingValue) throws Exception {

        String select = " ";

        if (groupingValue != null && !"".equals(groupingValue)) {

            if ("DeviceModel".equalsIgnoreCase(groupingValue)) {
                select += " count(d),dm.id,dm.name ";
            }
            if ("Vendor".equalsIgnoreCase(groupingValue)) {
                select += " count(d),dv.id,dv.name ";
            }

        } else {
        }

        return select;
    }

    private String setGroupByCondition(String groupingValue) throws Exception {

        String groupBy = " ";
        if (groupingValue != null && !"".equalsIgnoreCase(groupingValue)) {
            if ("DeviceModel".equalsIgnoreCase(groupingValue)) {
                groupBy += " dm.id";
            }
            if ("Vendor".equalsIgnoreCase(groupingValue)) {
                groupBy += " dv.id";
            }
        } else {
        }
        return groupBy;
    }

    private void extracted(List<DistributionResponseDto> responseData, List<Object[]> list) {
        IntStream.range(0, list.size())
                .forEach(i -> {
                    DistributionResponseDto dto = new DistributionResponseDto();
                    dto.setCount((Long) list.get(i)[0]);
                    dto.setId((Integer) list.get(i)[1]);
                    dto.setName((String) list.get(i)[2]);
                    responseData.add(dto);
                });
    }

}
