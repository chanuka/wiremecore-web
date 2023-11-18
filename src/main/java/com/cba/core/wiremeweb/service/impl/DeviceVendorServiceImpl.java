package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceVendorRequestDto;
import com.cba.core.wiremeweb.dto.DeviceVendorResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceVendorServiceImpl implements GenericService<DeviceVendorResponseDto, DeviceVendorRequestDto> {

    private final GenericDao<DeviceVendorResponseDto, DeviceVendorRequestDto> dao;
    private final UserBeanUtil userBeanUtil;

    @Override
    public Page<DeviceVendorResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<DeviceVendorResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<DeviceVendorResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    @Override
    public DeviceVendorResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public DeviceVendorResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public DeviceVendorResponseDto updateById(int id, DeviceVendorRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public DeviceVendorResponseDto create(DeviceVendorRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<DeviceVendorResponseDto> createBulk(List<DeviceVendorRequestDto> requestDtoList) throws Exception {
        return dao.createBulk(requestDtoList);
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
