package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.DeviceResponseDto;

import java.util.List;

public interface GlobalAuditEntryService {

    public void createNewRevision(String resource, Integer effectedId, String operation, String oldValue, String newValue) throws Exception;

    public void createNewRevisionListForDelete(String resource, List<Integer> effectedIdList, String operation) throws Exception;

    public void createNewRevisionListForCreate(String resource, List<DeviceResponseDto> effectedIdList, String operation) throws Exception;

}
