package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.model.Resource;

public class ResourceMapper {

    public static ResourceResponseDto toDto(Resource entity) {
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        return responseDto;
    }

}
