package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.DeviceModelDao;
import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dto.DeviceVendorRequestDto;
import com.cba.core.wiremeweb.dto.DeviceVendorResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceModelMapper;
import com.cba.core.wiremeweb.mapper.DeviceVendorMapper;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceVendorServiceImpl implements GenericService<DeviceVendorResponseDto, DeviceVendorRequestDto> {

    private final GenericDao<DeviceVendor> dao;
    private final DeviceModelDao<DeviceModel> modelDao;
    private final GlobalAuditDao globalAuditDao;
    private final UserBeanUtil userBeanUtil;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.device_vendor}")
    private String resource;

    @Override
    public Page<DeviceVendorResponseDto> findAll(int page, int pageSize) throws Exception {
        Page<DeviceVendor> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceVendorMapper::toDto);
    }

    @Override
    public List<DeviceVendorResponseDto> findAll() throws Exception {
        List<DeviceVendor> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entityList
                .stream()
                .map(DeviceVendorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceVendorResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize).map(DeviceVendorMapper::toDto);
    }

    @Override
    public Page<DeviceVendorResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLikeByKeyWord(searchParameter, page, pageSize).map(DeviceVendorMapper::toDto);
    }

    @Override
    public DeviceVendorResponseDto findById(int id) throws Exception {
        DeviceVendor entity = dao.findById(id);
        List<DeviceModel> deviceModels = modelDao.findAllByDeviceVendor_Id(entity.getId());

        DeviceVendorResponseDto deviceVendorResponseDto = DeviceVendorMapper.toDto(entity);
        deviceVendorResponseDto.setDeviceModels(deviceModels.stream().map(DeviceModelMapper::toDto).collect(Collectors.toList()));
        return deviceVendorResponseDto;
    }

    @Override
    public DeviceVendorResponseDto deleteById(int id) throws Exception {
        DeviceVendor entity = dao.findById(id);
        DeviceVendorResponseDto responseDto = DeviceVendorMapper.toDto(entity);

        dao.deleteById(id);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
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
                        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                item, objectMapper.writeValueAsString(objectNode), null,
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                    }
                });
    }

    @Override
    public DeviceVendorResponseDto updateById(int id, DeviceVendorRequestDto requestDto) throws Exception {
        DeviceVendor toBeUpdated = dao.findById(id);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!requestDto.getName().equals(toBeUpdated.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!requestDto.getImg().equals(toBeUpdated.getImg())) {
            updateRequired = true;
            oldDataMap.put("img", toBeUpdated.getImg());
            newDataMap.put("img", requestDto.getImg());

            toBeUpdated.setImg(requestDto.getImg());
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

            return DeviceVendorMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public DeviceVendorResponseDto create(DeviceVendorRequestDto requestDto) throws Exception {
        DeviceVendor toInsert = DeviceVendorMapper.toModel(requestDto);
        toInsert.setStatus(new Status(requestDto.getStatus()));
        DeviceVendor savedEntity = dao.create(toInsert);
        DeviceVendorResponseDto responseDto = DeviceVendorMapper.toDto(savedEntity);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<DeviceVendorResponseDto> createBulk(List<DeviceVendorRequestDto> requestDtoList) throws Exception {
        List<DeviceVendor> entityList = requestDtoList
                .stream()
                .map(DeviceVendorMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    DeviceVendorResponseDto responseDto = DeviceVendorMapper.toDto(item);
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
        return new byte[0];
    }

    @Override
    public byte[] exportExcelReport() throws Exception {
        return new byte[0];
    }
}
