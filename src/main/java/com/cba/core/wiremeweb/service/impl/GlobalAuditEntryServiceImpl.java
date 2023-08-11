package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditEntryDao;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.service.GlobalAuditEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalAuditEntryServiceImpl implements GlobalAuditEntryService {

    private final GlobalAuditEntryDao globalAuditEntryDao;
    private final HttpServletRequest request;

    public void createNewRevision(String resource, Integer effectedId, String operation
            , String oldValue, String newValue) throws Exception {

        GlobalAuditEntry globalAuditEntry = globalAuditEntryDao.createRevision(resource, effectedId, operation,
                oldValue, newValue, getIpAddress());

        globalAuditEntryDao.saveRevision(globalAuditEntry);
    }

    public void createNewRevisionListForDelete(String resource, List<Integer> effectedIdList, String operation) throws Exception {

        List<GlobalAuditEntry> globalAuditEntryList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Integer id : effectedIdList) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("id", id);
            GlobalAuditEntry globalAuditEntry = globalAuditEntryDao.createRevision(resource, id, operation,
                    objectMapper.writeValueAsString(objectNode), null, getIpAddress());
            globalAuditEntryList.add(globalAuditEntry);
        }
        globalAuditEntryDao.saveAllRevision(globalAuditEntryList);
    }

    public void createNewRevisionListForCreate(String resource, List<DeviceResponseDto> effectedList, String operation) throws Exception {

        List<GlobalAuditEntry> globalAuditEntryList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (DeviceResponseDto deviceResponseDto : effectedList) {
            GlobalAuditEntry globalAuditEntry = globalAuditEntryDao.createRevision(resource, deviceResponseDto.getId(), operation,
                    null, objectMapper.writeValueAsString(deviceResponseDto), getIpAddress());
            globalAuditEntryList.add(globalAuditEntry);
        }
        globalAuditEntryDao.saveAllRevision(globalAuditEntryList);
    }

    private String getIpAddress() {
        return request.getRemoteAddr();
    }
}
