package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GeneralDao;
import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DistrictMapper;
import com.cba.core.wiremeweb.model.District;
import com.cba.core.wiremeweb.service.GeneralService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final GeneralDao dao;

    @Override
    public Map<String, List<DistrictDto>> findAllDistrict() throws Exception {

        List<District> districts = dao.findAllDistrict();
        Map<String, List<DistrictDto>> districtResponseDtoMap = new HashMap();

        if (districts.isEmpty()) {
            throw new NotFoundException("No District found");
        }
        districts.forEach(district -> {
            List<DistrictDto> s = null;
            if ((s = districtResponseDtoMap.get(district.getProvince().getCode())) != null) {
                s.add(DistrictMapper.toDto(district));
                districtResponseDtoMap.put(district.getProvince().getCode(),s);
            } else {
                List<DistrictDto> districtList = new ArrayList<>();
                districtList.add(DistrictMapper.toDto(district));
                districtResponseDtoMap.put(district.getProvince().getCode(),districtList);
            }
        });
        return districtResponseDtoMap;
    }
}
