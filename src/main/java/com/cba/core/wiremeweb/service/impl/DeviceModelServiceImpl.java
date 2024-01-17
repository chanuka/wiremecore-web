package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dto.DeviceModelRequestDto;
import com.cba.core.wiremeweb.dto.DeviceModelResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceModelMapper;
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
public class DeviceModelServiceImpl implements GenericService<DeviceModelResponseDto, DeviceModelRequestDto> {

    private final GenericDao<DeviceModel> dao;
    private final GenericDao<DeviceVendor> deviceVendorDao;
    private final GlobalAuditDao globalAuditDao;
    private final UserBeanUtil userBeanUtil;
    private final ObjectMapper objectMapper;
    @Value("${application.resource.device_model}")
    private String resource;

    @Override
    public Page<DeviceModelResponseDto> findAll(int page, int pageSize) throws Exception {

        Page<DeviceModel> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Device Model found");
        }
        return entitiesPage.map(DeviceModelMapper::toDto);
    }

    @Override
    public List<DeviceModelResponseDto> findAll() throws Exception {
        List<DeviceModel> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entityList
                .stream()
                .map(DeviceModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceModelResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize).map(DeviceModelMapper::toDto);
    }

    @Override
    public Page<DeviceModelResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLikeByKeyWord(searchParameter, page, pageSize).map(DeviceModelMapper::toDto);
    }

    @Override
    public DeviceModelResponseDto findById(int id) throws Exception {
        return DeviceModelMapper.toDto(dao.findById(id));
    }

    @Override
    public DeviceModelResponseDto deleteById(int id) throws Exception {
        DeviceModel entity = dao.findById(id);
        DeviceModelResponseDto responseDto = DeviceModelMapper.toDto(entity);

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
    public DeviceModelResponseDto updateById(int id, DeviceModelRequestDto requestDto) throws Exception {

        DeviceModel toBeUpdated = dao.findById(id);

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
        if (toBeUpdated.getDeviceVendor().getId() != requestDto.getDeviceVendor()) {
            updateRequired = true;
            oldDataMap.put("deviceVendor", toBeUpdated.getDeviceVendor().getId());
            newDataMap.put("deviceVendor", requestDto.getDeviceVendor());

            toBeUpdated.setDeviceVendor(deviceVendorDao.findById(requestDto.getDeviceVendor()));
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

            return DeviceModelMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public DeviceModelResponseDto create(DeviceModelRequestDto requestDto) throws Exception {
        DeviceModel toInsert = DeviceModelMapper.toModel(requestDto);
        toInsert.setDeviceVendor(deviceVendorDao.findById(requestDto.getDeviceVendor()));

        DeviceModel savedEntity = dao.create(toInsert);
        DeviceModelResponseDto responseDto = DeviceModelMapper.toDto(savedEntity);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;

    }

    @Override
    public List<DeviceModelResponseDto> createBulk(List<DeviceModelRequestDto> requestDtoList) throws Exception {
        List<DeviceModel> entityList = requestDtoList
                .stream()
                .map(DeviceModelMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    DeviceModelResponseDto responseDto = DeviceModelMapper.toDto(item);
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
