package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.repository.DeviceModelRepository;
import com.cba.core.wiremeweb.repository.DeviceVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DeviceVendorDaoImpl implements GenericDao<DeviceVendor> {

    private final DeviceVendorRepository repository;
    private final DeviceModelRepository deviceModelRepository;


    @Override
    public Page<DeviceVendor> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public List<DeviceVendor> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<DeviceVendor> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Page<DeviceVendor> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public DeviceVendor findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Device Vendor not found"));
    }

    @Override
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    public DeviceVendor updateById(int id, DeviceVendor requestDto) throws Exception {
        return repository.saveAndFlush(requestDto);
    }

    @Override
    public DeviceVendor create(DeviceVendor requestDto) throws Exception {
        return repository.save(requestDto);
    }

    @Override
    public List<DeviceVendor> createBulk(List<DeviceVendor> requestDtoList) throws Exception {
        return repository.saveAll(requestDtoList);
    }
}
