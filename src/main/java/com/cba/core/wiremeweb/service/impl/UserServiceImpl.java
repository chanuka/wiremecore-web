package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.service.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserBean userBean;

    @Override
    public Page<UserResponseDto> findAll(int page, int pageSize) throws Exception {
        return userDao.findAll(page, pageSize);
    }

    @Override
    public List<UserResponseDto> findAll() throws Exception {
        return userDao.findAll();
    }

    @Override
    public Page<UserResponseDto> findByUserNameLike(String userName, int page, int pageSize) throws Exception {
        return userDao.findByUserNameLike(userName, page, pageSize);
    }

    @Override
    public UserResponseDto findById(int id) throws Exception {
        return userDao.findById(id);
    }

    @Override
    public UserResponseDto deleteById(int id) throws Exception {
        return userDao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> userList) throws Exception {
        userDao.deleteByIdList(userList);
    }

    @Override
    public UserResponseDto updateById(int id, UserRequestDto userRequestDto) throws Exception {
        return userDao.updateById(id, userRequestDto);
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) throws Exception {
        return userDao.create(userRequestDto);
    }

    @Override
    public List<UserResponseDto> createBulk(List<UserRequestDto> userRequestDtoList) throws Exception {
        return userDao.createBulk(userRequestDtoList);
    }

    @Override
    public byte[] exportReport() throws Exception {
        List<UserResponseDto> userList = userDao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/user.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
