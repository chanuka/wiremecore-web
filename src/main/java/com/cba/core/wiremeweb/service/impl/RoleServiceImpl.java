package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.RoleDao;
import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import com.cba.core.wiremeweb.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final UserBean userBean;

    @Override
    public Page<RoleResponseDto> findAll(int page, int pageSize) throws Exception {
        return roleDao.findAll(page, pageSize);
    }

    @Override
    public List<RoleResponseDto> findAll() throws Exception {
        return roleDao.findAll();
    }

    @Override
    public Page<RoleResponseDto> findByRoleNameLike(String roleName, int page, int pageSize) throws Exception {
        return roleDao.findByRoleNameLike(roleName, page, pageSize);
    }

    @Override
    public RoleResponseDto findById(int id) throws Exception {
        return roleDao.findById(id);
    }

    @Override
    public RoleResponseDto deleteById(int id) throws Exception {
        return roleDao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> roleList) throws Exception {
        roleDao.deleteByIdList(roleList);

    }

    @Override
    public RoleResponseDto updateById(int id, RoleRequestDto roleRequestDto) throws Exception {
        return roleDao.updateById(id, roleRequestDto);
    }

    @Override
    public RoleResponseDto create(RoleRequestDto roleRequestDto) throws Exception {
        return roleDao.create(roleRequestDto);
    }

    @Override
    public List<RoleResponseDto> createBulk(List<RoleRequestDto> roleRequestDtoList) throws Exception {
        return roleDao.createBulk(roleRequestDtoList);
    }

    @Override
    public byte[] exportReport() throws Exception {

        List<RoleResponseDto> roleList = roleDao.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/role.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(roleList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBean.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
