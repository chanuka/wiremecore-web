package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceModelDao;
import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceModelRequestDto;
import com.cba.core.wiremeweb.dto.DeviceModelResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceModelMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.DeviceModelRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeviceModelDaoImpl implements DeviceModelDao<DeviceModel, DeviceModel> {

    private final DeviceModelRepository repository;

    @Override
    public Page<DeviceModel> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public List<DeviceModel> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<DeviceModel> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Page<DeviceModel> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public DeviceModel findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Device Model not found"));
    }

    @Override
    public DeviceModel deleteById(int id) throws Exception {
        repository.deleteById(id);
        return new DeviceModel();
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    public DeviceModel updateById(int id, DeviceModel deviceModel) throws Exception {
        return repository.saveAndFlush(deviceModel);
    }

    @Override
    public DeviceModel create(DeviceModel deviceModel) throws Exception {
        return repository.save(deviceModel);
    }

    @Override
    public List<DeviceModel> createBulk(List<DeviceModel> deviceModelList) throws Exception {
        return repository.saveAll(deviceModelList);
    }

    @Override
    public List<DeviceModel> findAllByDeviceVendor_Id(int VendorId) throws Exception {
        return repository.findAllByDeviceVendor_Id(VendorId);
    }
}
