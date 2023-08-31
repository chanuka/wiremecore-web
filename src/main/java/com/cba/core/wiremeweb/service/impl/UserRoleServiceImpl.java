package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.UserRoleRequestDto;
import com.cba.core.wiremeweb.dto.UserRoleResponseDto;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBean;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements GenericService<UserRoleResponseDto, UserRoleRequestDto> {

    private final GenericDao<UserRoleResponseDto, UserRoleRequestDto> dao;
    private final UserBean userBean;

    @Override
    public Page<UserRoleResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<UserRoleResponseDto> findAll() throws Exception {
        return dao.findAll();
    }

    @Override
    public Page<UserRoleResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public UserRoleResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public UserRoleResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        dao.deleteByIdList(idList);
    }

    @Override
    public UserRoleResponseDto updateById(int id, UserRoleRequestDto requestDto) throws Exception {
        return dao.updateById(id, requestDto);
    }

    @Override
    public UserRoleResponseDto create(UserRoleRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public List<UserRoleResponseDto> createBulk(List<UserRoleRequestDto> requestDtoList) throws Exception {
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
