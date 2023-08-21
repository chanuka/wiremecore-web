package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.RoleResource;
import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import com.cba.core.wiremeweb.exception.InternalServerError;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RoleController implements RoleResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final RoleService roleService;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<RoleResponseDto>> roles(int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("ROLE_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<RoleResponseDto> list = roleService.findAll(page, pageSize);
            return ResponseEntity.ok().body(list.getContent());
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<RoleResponseDto> getARole(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("ROLE_GET_ONE_DEBUG", null, currentLocale));

        RoleResponseDto roleResponseDto = null;
        try {
            roleResponseDto = roleService.findById(id);

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
        return ResponseEntity.ok().body(roleResponseDto);
    }

    @Override
    public ResponseEntity<List<RoleResponseDto>> searchRoles(String roleName, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<RoleResponseDto> userList = roleService.findByRoleNameLike(roleName, page, pageSize);
            return ResponseEntity.ok().body(userList.getContent());
        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> deleteARole(int id) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DELETE_ONE_DEBUG", null, currentLocale));

        try {

            RoleResponseDto roleResponseDto = roleService.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("ROLE_DELETE_ONE_SUCCESS", null, currentLocale));

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (RecordInUseException ru) {
            logger.error(ru.getMessage());
            throw ru;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<RoleResponseDto> updateARole(int id, RoleRequestDto roleRequestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            RoleResponseDto response = roleService.updateById(id, roleRequestDto);
            return ResponseEntity.ok().body(response);

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<RoleResponseDto> createARole(RoleRequestDto roleRequestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_CREATE_ONE_DEBUG", null, currentLocale));
        try {

            RoleResponseDto roleResponseDto = roleService.create(roleRequestDto);
            return ResponseEntity.ok().body(roleResponseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> createRoles(List<RoleRequestDto> list) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<RoleResponseDto> userList = roleService.createBulk(list);
            return ResponseEntity.ok().body(messageSource.getMessage("ROLE_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> deleteRoles(List<Integer> roleIdList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            roleService.deleteByIdList(roleIdList);
            return ResponseEntity.ok().body(messageSource.getMessage("ROLE_DELETE_ALL_SUCCESS", null, currentLocale));

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (RecordInUseException ru) {
            logger.error(ru.getMessage());
            throw ru;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel() throws IOException {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");

            String[] columnHeaders = {"Id", "Role Name", "Status"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            List<RoleResponseDto> list = roleService.findAll();

            int rowCount = 1;

            for (RoleResponseDto roleResponseDto : list) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell cell = row.createCell(columnCount++);
                if (roleResponseDto.getId() instanceof Integer) {
                    cell.setCellValue((Integer) roleResponseDto.getId());
                }
                cell = row.createCell(columnCount++);
                if (roleResponseDto.getRoleName() instanceof String) {
                    cell.setCellValue((String) roleResponseDto.getRoleName());
                }
                cell = row.createCell(columnCount++);
                if (roleResponseDto.getStatus() instanceof String) {
                    cell.setCellValue((String) roleResponseDto.getStatus());
                }

            }

            byte[] excelBytes;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                excelBytes = outputStream.toByteArray();
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "Role_List.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(httpHeaders)
                    .body(excelBytes);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadJasper() throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("ROLE_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "role-details.pdf");
            return new ResponseEntity<byte[]>(roleService.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }
}
