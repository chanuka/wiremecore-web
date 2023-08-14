package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.util.UpdateResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DeviceService {

    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception;

    public List<DeviceResponseDto> findAll() throws Exception;

    public Page<DeviceResponseDto> findBySerialNoLike(String serialNumber,int page, int pageSize) throws Exception;

    public DeviceResponseDto findById(int id) throws Exception;

    public DeviceResponseDto deleteById(int id) throws Exception;

    public void deleteByIdList(List<Integer> deviceList) throws Exception;

    public DeviceResponseDto updateById(int id, DeviceRequestDto deviceRequestDto) throws Exception;

    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws Exception;

    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> deviceRequestDto) throws Exception;

    public byte[] exportReport() throws FileNotFoundException, JRException,Exception;

}
