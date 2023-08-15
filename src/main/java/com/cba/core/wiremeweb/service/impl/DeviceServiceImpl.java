package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.DeviceDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceDao deviceDao;
    private final UserBean userBean;


    @Override
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {
        return deviceDao.findAll(page, pageSize);
    }

    @Override
    public List<DeviceResponseDto> findAll() throws Exception {
        return deviceDao.findAll();
    }

    @Override
    public Page<DeviceResponseDto> findBySerialNoLike(String serialNumber, int page, int pageSize) throws Exception {
        return deviceDao.findBySerialNoLike(serialNumber, page, pageSize);
    }

    public DeviceResponseDto findById(int id) throws Exception {
        return deviceDao.findById(id);
    }

    @Override
    public DeviceResponseDto deleteById(int id) throws Exception {
        return deviceDao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> deviceList) throws Exception {
        deviceDao.deleteByIdList(deviceList);
    }

    @Override
    public DeviceResponseDto updateById(int id, DeviceRequestDto deviceRequestDto) throws Exception {
        return deviceDao.updateById(id, deviceRequestDto);
    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws Exception {
        return deviceDao.create(deviceRequestDto);
    }

    @Override
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> deviceRequestDto) throws Exception {
        return deviceDao.createBulk(deviceRequestDto);
    }

    @Override
    public byte[] exportReport() throws FileNotFoundException, JRException, Exception {
        List<DeviceResponseDto> deviceList = deviceDao.findAll();
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
