package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.UserMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.service.EmailService;
import com.cba.core.wiremeweb.service.UserService;
import com.cba.core.wiremeweb.util.DeviceTypeEnum;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.cba.core.wiremeweb.util.UserPasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService<UserResponseDto, UserRequestDto> {

    private final UserDao<User> dao;
    private final GlobalAuditDao globalAuditDao;
    private final UserBeanUtil userBeanUtil;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${application.resource.users}")
    private String resource;

    @Override
    public Page<UserResponseDto> findAll(int page, int pageSize) throws Exception {
        Page<User> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entitiesPage.map(UserMapper::toDto);
    }

    @Override
    public List<UserResponseDto> findAll() throws Exception {
        List<User> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entityList
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Page<User> entitiesPage = dao.findBySearchParamLike(searchParamList, page, pageSize);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entitiesPage.map(UserMapper::toDto);
    }

    @Override
    public Page<UserResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public UserResponseDto findById(int id) throws Exception {
        User entity = dao.findById(id);
        return UserMapper.toDto(entity);
    }

    @Override
    public UserResponseDto deleteById(int id) throws Exception {
        User entity = dao.findById(id);
        UserResponseDto responseDto = UserMapper.toDto(entity);

        dao.deleteById(id);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                id, objectMapper.writeValueAsString(responseDto), null,
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {

        idList.stream().forEach((id) -> {
            try {
                dao.findById(id);
            } catch (Exception exception) {
                throw new NotFoundException(exception.getMessage());
            }
        });

        dao.deleteByIdList(idList);

        idList.stream()
                .forEach(item -> {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("id", item);
                    try {
                        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                item, objectMapper.writeValueAsString(objectNode), null,
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                    }
                });
    }

    @Override
    public UserResponseDto updateById(int id, UserRequestDto requestDto) throws Exception {
        User toBeUpdated = dao.findById(id);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getUserName().equals(requestDto.getUserName())) {
            updateRequired = true;
            oldDataMap.put("userName", toBeUpdated.getUserName());
            newDataMap.put("userName", requestDto.getUserName());

            toBeUpdated.setUserName(requestDto.getUserName());
        }
        if (!toBeUpdated.getContactNo().equals(requestDto.getContactNo())) {
            updateRequired = true;
            oldDataMap.put("contactNo", toBeUpdated.getContactNo());
            newDataMap.put("contactNo", requestDto.getContactNo());

            toBeUpdated.setContactNo(requestDto.getContactNo());
        }
        if (!toBeUpdated.getEmail().equals(requestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", requestDto.getEmail());

            toBeUpdated.setEmail(requestDto.getEmail());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(requestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", requestDto.getStatus());

            toBeUpdated.setStatus(new Status(requestDto.getStatus()));
        }


        if ((toBeUpdated.getDevice() != null ? toBeUpdated.getDevice().getId() : null) != requestDto.getDeviceId()) {
            updateRequired = true;
            oldDataMap.put("deviceId", (toBeUpdated.getDevice() != null ? toBeUpdated.getDevice().getId() : null));
            newDataMap.put("deviceId", requestDto.getDeviceId());
            if (requestDto.getDeviceId() != null) {
                toBeUpdated.setDevice(new Device(requestDto.getDeviceId()));
            } else {
                toBeUpdated.setDevice(null);
            }
        }
        if ((toBeUpdated.getMerchant() != null ? toBeUpdated.getMerchant().getId() : null) != requestDto.getMerchantId()) {
            updateRequired = true;
            oldDataMap.put("merchantId", (toBeUpdated.getMerchant() != null ? toBeUpdated.getMerchant().getId() : null));
            newDataMap.put("merchantId", requestDto.getMerchantId());
            if (requestDto.getMerchantId() != null) {
                toBeUpdated.setMerchant(new Merchant(requestDto.getMerchantId()));
            } else {
                toBeUpdated.setMerchant(null);
            }
        }
        if ((toBeUpdated.getMerchantCustomer() != null ? toBeUpdated.getMerchantCustomer().getId() : null) != requestDto.getPartnerId()) {
            updateRequired = true;
            oldDataMap.put("partnerId", (toBeUpdated.getMerchantCustomer() != null ? toBeUpdated.getMerchantCustomer().getId() : null));
            newDataMap.put("partnerId", requestDto.getPartnerId());
            if (requestDto.getPartnerId() != null) {
                toBeUpdated.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
            } else {
                toBeUpdated.setMerchantCustomer(null);
            }
        }
        if (updateRequired) {

            dao.updateById(id, toBeUpdated);
            globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return UserMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public UserResponseDto create(UserRequestDto requestDto) throws Exception {
        char[] pwd = UserPasswordUtil.generateCommonLangPassword().toCharArray();
        User toInsert = UserMapper.toModel(requestDto);
        toInsert.setPassword(new BCryptPasswordEncoder().encode(pwd.toString()));

        User savedEntity = dao.create(toInsert);
        UserResponseDto responseDto = UserMapper.toDto(savedEntity);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        String message = "Your Password for Wireme is : " + pwd.toString();
        emailService.sendEmail(toInsert.getEmail(), message);

        return responseDto;
    }

    @Override
    public List<UserResponseDto> createBulk(List<UserRequestDto> requestDtoList) throws Exception {
        List<User> entityList = requestDtoList
                .stream()
                .map(UserMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
                .stream()
                .map(item -> {
                    UserResponseDto responseDto = UserMapper.toDto(item);
                    try {
                        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportPdfReport() throws Exception {
        List<UserResponseDto> responseDtoList = dao.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList()
                );
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/user.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(responseDtoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Created By :" + userBeanUtil.getUsername()); // username can be extracted once the url is accessible

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] exportExcelReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        String[] columnHeaders = {"Id", "Name", "User Name", "Contact No", "Email"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        List<UserResponseDto> responseDtoList = dao.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList()
                );

        int rowCount = 1;

        for (UserResponseDto responseDto : responseDtoList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            Cell cell = row.createCell(columnCount++);
            if (responseDto.getId() instanceof Integer) {
                cell.setCellValue((Integer) responseDto.getId());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getName() instanceof String) {
                cell.setCellValue((String) responseDto.getName());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getUserName() instanceof String) {
                cell.setCellValue((String) responseDto.getUserName());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getContactNo() instanceof String) {
                cell.setCellValue((String) responseDto.getContactNo());
            }
            cell = row.createCell(columnCount++);
            if (responseDto.getEmail() instanceof String) {
                cell.setCellValue((String) responseDto.getEmail());
            }
        }

        byte[] excelBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            excelBytes = outputStream.toByteArray();
        }
        return excelBytes;
    }

    @Override
    public String changePassword(ChangePasswordRequestDto requestDto) throws Exception {

        Map<String, Object> map = new HashMap<>();

        UserType userType = new UserType();
        userType.setId(DeviceTypeEnum.WEB.getValue());

        User entity = dao.findByUserName(userBeanUtil.getUsername());

        if (passwordEncoder.matches(requestDto.getCurrentPassword(), entity.getPassword())) {
            entity.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            dao.updateById(entity.getId(), entity);
            map.put("password", "xxxxxxxx");
            String maskValue = objectMapper.writeValueAsString(map);

            globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    entity.getId(), maskValue, maskValue,
                    userBeanUtil.getRemoteAdr()));
        } else {
            throw new NotFoundException("Fail - Old Password mismatch");
        }

        return "success";
    }

    @Override
    public String accountLockReset(String userName) throws Exception {
        User toBeUpdated = dao.findByUserName(userName);
        Map<String, Object> oldValueMap = new HashMap<>();
        Map<String, Object> newValueMap = new HashMap<>();
        oldValueMap.put("loginAttempt", toBeUpdated.getLoginAttempt());
        oldValueMap.put("status", toBeUpdated.getStatus().getStatusCode());

        toBeUpdated.setLoginAttempt(0);
        toBeUpdated.setStatus(new Status("ACTV"));
        newValueMap.put("loginAttempt", toBeUpdated.getLoginAttempt());
        newValueMap.put("status", toBeUpdated.getStatus().getStatusCode());

        dao.updateById(toBeUpdated.getId(), toBeUpdated);

        /*
        Old and new values are not set to JSON
         */
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                toBeUpdated.getId(), objectMapper.writeValueAsString(oldValueMap), objectMapper.writeValueAsString(newValueMap),
                userBeanUtil.getRemoteAdr()));
        return "success";
    }
}
