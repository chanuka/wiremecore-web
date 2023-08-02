package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DeviceDao {

    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws SQLException;

    public List<DeviceResponseDto> findAll() throws SQLException;

    public Page<DeviceResponseDto> findBySerialNoLike(String serialNumber, int page, int pageSize) throws SQLException;

    public DeviceResponseDto findById(int id) throws SQLException;

    public void deleteById(int id) throws SQLException;

    public void deleteByIdList(List<Map<String, Integer>> deviceList) throws SQLException;

    public DeviceResponseDto updateById(int id, DeviceRequestDto deviceRequestDto) throws SQLException;

    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws SQLException;

    public void createBulk(List<DeviceRequestDto> deviceRequestDtoList) throws SQLException;
}
