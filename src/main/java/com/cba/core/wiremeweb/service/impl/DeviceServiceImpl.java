package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dao.impl.DeviceDaoImpl;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.service.DeviceService;
import com.cba.core.wiremeweb.util.UserBean;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceDaoImpl deviceDaoImpl;
    private final UserBean userBean;


    @Override
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws SQLException {
        return deviceDaoImpl.findAll(page, pageSize);
    }

    @Override
    public List<DeviceResponseDto> findAll() throws SQLException {
        return deviceDaoImpl.findAll();
    }

    @Override
    public Page<DeviceResponseDto> findBySerialNoLike(String serialNumber, int page, int pageSize) throws SQLException {
        return deviceDaoImpl.findBySerialNoLike(serialNumber, page, pageSize);
    }

    public DeviceResponseDto findById(int id) throws SQLException {
        return deviceDaoImpl.findById(id);
    }

    @Override
    public void deleteById(int id) throws SQLException {
        deviceDaoImpl.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Map<String, Integer>> deviceList) throws SQLException {
        deviceDaoImpl.deleteByIdList(deviceList);
    }

    @Override
    public DeviceResponseDto updateById(int id, DeviceRequestDto deviceRequestDto) throws SQLException {
        return deviceDaoImpl.updateById(id, deviceRequestDto);
    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws SQLException {
        return deviceDaoImpl.create(deviceRequestDto);
    }

    @Override
    public void createBulk(List<DeviceRequestDto> deviceRequestDto) throws SQLException {
        deviceDaoImpl.createBulk(deviceRequestDto);
    }

    @Override
    public byte[] exportReport() throws FileNotFoundException, JRException, SQLException {
        List<DeviceResponseDto> deviceList = deviceDaoImpl.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/device.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(deviceList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
