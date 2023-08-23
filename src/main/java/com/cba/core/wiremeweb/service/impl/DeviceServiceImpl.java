package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBean;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements GenericService<DeviceResponseDto,DeviceRequestDto> {

    private final GenericDao<DeviceResponseDto, DeviceRequestDto> dao;
    private final UserBean userBean;


    @Override
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<DeviceResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<DeviceResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    public DeviceResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public DeviceResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public DeviceResponseDto updateById(int id, DeviceRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> requestDtolist) throws Exception {
        return dao.createBulk(requestDtolist);
    }

    @Override
    public byte[] exportReport() throws Exception {
        List<DeviceResponseDto> ResponseDtoList = dao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/device.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ResponseDtoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
