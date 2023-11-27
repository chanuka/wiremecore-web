package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.model.District;

public class DistrictMapper {

    public static DistrictDto toDto(District district) {
        DistrictDto responseDto = new DistrictDto();
        responseDto.setCode(district.getCode());
        responseDto.setName(district.getDescription());
        responseDto.setLat(district.getLat());
        responseDto.setLon(district.getLon());
        return responseDto;
    }
}
