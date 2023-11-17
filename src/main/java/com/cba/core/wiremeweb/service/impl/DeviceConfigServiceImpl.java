package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.DeviceConfigDao;
import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.service.DeviceConfigService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceConfigServiceImpl implements DeviceConfigService {

    private final DeviceConfigDao dao;
    private final UserBeanUtil userBeanUtil;

    @Override
    public DeviceConfigResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }

    @Override
    public DeviceConfigResponseDto create(DeviceConfigRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }

    @Override
    public DeviceConfigResponseDto update(int id, DeviceConfigRequestDto requestDto) throws Exception {
        return dao.update(id, requestDto);
    }

    @Override
    public DeviceConfigResponseDto deleteById(int id) throws Exception {
        return dao.deleteById(id);
    }
}
