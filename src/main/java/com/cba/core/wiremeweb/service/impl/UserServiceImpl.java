package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
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
public class UserServiceImpl implements GenericService<UserResponseDto, UserRequestDto> {

    private final GenericDao<UserResponseDto, UserRequestDto> dao;
    private final UserBean userBean;

    @Override
    public Page<UserResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<UserResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<UserResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return dao.findBySearchParamLike(searchParamList, page, pageSize);
    }

    @Override
    public UserResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public UserResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public UserResponseDto updateById(int id, UserRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public UserResponseDto create(UserRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<UserResponseDto> createBulk(List<UserRequestDto> requestDtoList) throws Exception {
        return dao.createBulk(requestDtoList);
    }

    @Override
    public byte[] exportReport() throws Exception {
        List<UserResponseDto> responseDtoList = dao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/user.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(responseDtoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
