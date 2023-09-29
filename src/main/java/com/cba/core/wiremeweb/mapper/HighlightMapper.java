package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.HighlightRequestDto;
import com.cba.core.wiremeweb.dto.HighlightResponseDto;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserConfig;

public class HighlightMapper {


    public static HighlightResponseDto toDto(HighlightResponseDto responseDto, UserConfig userConfig) {
        responseDto.setPriorityOrder(userConfig.getPriorityOrder());
        responseDto.setStatus(userConfig.getStatus().getStatusCode());
        return responseDto;
    }

    public static UserConfig toModel(HighlightRequestDto requestDto, String config, User user) {
        UserConfig entity = new UserConfig();
        entity.setConfigType(requestDto.getConfigType());
        entity.setConfigName(requestDto.getConfigName());
        entity.setConfig(config);
        entity.setPriorityOrder(requestDto.getPriorityOrder());
        entity.setUser(user);
        Status status = new Status(requestDto.getStatus());
        entity.setStatus(status);
        return entity;
    }
}
