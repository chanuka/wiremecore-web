package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GeneralDao;
import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.service.GeneralService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final GeneralDao dao;
    private final UserBeanUtil userBeanUtil;

    @Override
    public Map<String, List<DistrictDto>> findAllDistrict() throws Exception {
        return dao.findAllDistrict();
    }
}
