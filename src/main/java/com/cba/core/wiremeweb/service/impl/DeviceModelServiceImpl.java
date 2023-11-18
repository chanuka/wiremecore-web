package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceModelRequestDto;
import com.cba.core.wiremeweb.dto.DeviceModelResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceModelServiceImpl implements GenericService<DeviceModelResponseDto, DeviceModelRequestDto> {

    private final GenericDao<DeviceModelResponseDto, DeviceModelRequestDto> dao;
    private final UserBeanUtil userBeanUtil;

    @Override
    public Page<DeviceModelResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<DeviceModelResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<DeviceModelResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    @Override
    public DeviceModelResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public DeviceModelResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public DeviceModelResponseDto updateById(int id, DeviceModelRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public DeviceModelResponseDto create(DeviceModelRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<DeviceModelResponseDto> createBulk(List<DeviceModelRequestDto> requestDtoList) throws Exception {
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
