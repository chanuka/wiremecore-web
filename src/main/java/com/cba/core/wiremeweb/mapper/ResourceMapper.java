package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.model.Resource;

public class ResourceMapper {

    public static ResourceResponseDto toDto(Resource resource) {
        ResourceResponseDto resourceResponseDto = new ResourceResponseDto();
        resourceResponseDto.setId(resource.getId());
        resourceResponseDto.setName(resource.getName());
        return resourceResponseDto;
    }

}
