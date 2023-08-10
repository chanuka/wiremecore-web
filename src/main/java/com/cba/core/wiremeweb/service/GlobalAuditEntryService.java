package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.dao.GlobalAuditEntryDao;
import com.cba.core.wiremeweb.service.impl.DeviceServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GlobalAuditEntryService {

    private final GlobalAuditEntryDao globalAuditEntryDao;
    private final HttpServletRequest request;

    public void createNewRevision(String resource, Integer effectedId, String operation
            , String oldValue, String newValue) throws Exception {

        GlobalAuditEntry revisionEntity = new GlobalAuditEntry();

        globalAuditEntryDao.newRevision(revisionEntity, resource, effectedId, operation,
                oldValue, newValue, getIpAddress());
    }

    private String getIpAddress() {
        return request.getRemoteAddr();
    }

//    private String getUsername() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return ((UserDetails) principal).getUsername();
//        }
//        return principal.toString();
//    }

//    public List<String> findModifiedFieldsUsingReflection(DeviceResponseDto originalDto, DeviceRequestDto updatedDto) throws IllegalAccessException {
//        List<String> modifiedFields = new ArrayList<>();
//        Field[] fields = DeviceRequestDto.class.getDeclaredFields();
//
//
//        for (Field field : fields) {
//            field.setAccessible(true);
//            Object originalValue = field.get(originalDto);
//            Object updatedValue = field.get(updatedDto);
//
//            if (!Objects.equals(originalValue, updatedValue)) {
//                modifiedFields.add(field.getName());
//            }
//        }
//        return modifiedFields;
//    }
}
