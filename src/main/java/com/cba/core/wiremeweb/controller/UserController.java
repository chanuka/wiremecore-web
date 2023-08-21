package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.UserResource;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.exception.InternalServerError;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.service.UserService;
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
public class UserController implements UserResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final UserService userService;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<UserResponseDto>> users(int page, int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("USER_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<UserResponseDto> list = userService.findAll(page, pageSize);
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
    public ResponseEntity<UserResponseDto> getAUser(int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("USER_GET_ONE_DEBUG", null, currentLocale));

        UserResponseDto userResponseDto = null;
        try {
            userResponseDto = userService.findById(id);

        } catch (NotFoundException nf) {
            logger.error(nf.getMessage());
            throw nf;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
        return ResponseEntity.ok().body(userResponseDto);
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> searchUsers(String userName, int page, int pageSize) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_GET_SEARCH_DEBUG", null, currentLocale));

        try {
            Page<UserResponseDto> userList = userService.findByUserNameLike(userName, page, pageSize);
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
    public ResponseEntity<String> deleteAUser(int id) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_DELETE_ONE_DEBUG", null, currentLocale));

        try {

            UserResponseDto userResponseDto = userService.deleteById(id);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_DELETE_ONE_SUCCESS", null, currentLocale));

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
    public ResponseEntity<UserResponseDto> updateAUser(int id, UserRequestDto userRequestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_UPDATE_ONE_DEBUG", null, currentLocale));
        try {
            UserResponseDto response = userService.updateById(id, userRequestDto);
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
    public ResponseEntity<UserResponseDto> createAUser(UserRequestDto userRequestDto) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_CREATE_ONE_DEBUG", null, currentLocale));
        try {

            UserResponseDto userResponseDto = userService.create(userRequestDto);
            return ResponseEntity.ok().body(userResponseDto);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> createUsers(List<UserRequestDto> userRequestDtoList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_CREATE_BULK_DEBUG", null, currentLocale));

        try {
            List<UserResponseDto> userList = userService.createBulk(userRequestDtoList);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_CREATE_ALL_SUCCESS", null, currentLocale));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<String> deleteUsers(List<Integer> userIdList) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("USER_DELETE_BULK_DEBUG", null, currentLocale));
        try {
            userService.deleteByIdList(userIdList);
            return ResponseEntity.ok().body(messageSource.getMessage("USER_DELETE_ALL_SUCCESS", null, currentLocale));

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
        logger.debug(messageSource.getMessage("USER_DOWNLOAD_EXCEL_DEBUG", null, currentLocale));

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");

            String[] columnHeaders = {"Id", "Name", "User Name", "Contact No", "Email"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            List<UserResponseDto> list = userService.findAll();

            int rowCount = 1;

            for (UserResponseDto userResponseDto : list) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell cell = row.createCell(columnCount++);
                if (userResponseDto.getId() instanceof Integer) {
                    cell.setCellValue((Integer) userResponseDto.getId());
                }
                cell = row.createCell(columnCount++);
                if (userResponseDto.getName() instanceof String) {
                    cell.setCellValue((String) userResponseDto.getName());
                }
                cell = row.createCell(columnCount++);
                if (userResponseDto.getUserName() instanceof String) {
                    cell.setCellValue((String) userResponseDto.getUserName());
                }
                cell = row.createCell(columnCount++);
                if (userResponseDto.getContactNo() instanceof String) {
                    cell.setCellValue((String) userResponseDto.getContactNo());
                }
                cell = row.createCell(columnCount++);
                if (userResponseDto.getEmail() instanceof String) {
                    cell.setCellValue((String) userResponseDto.getEmail());
                }
            }

            byte[] excelBytes;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                excelBytes = outputStream.toByteArray();
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "User_List.xlsx");

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
        logger.debug(messageSource.getMessage("USER_DOWNLOAD_PDF_DEBUG", null, currentLocale));

        try {
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "user-details.pdf");
            return new ResponseEntity<byte[]>(userService.exportReport(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerError(messageSource.getMessage("GLOBAL_INTERNAL_SERVER_ERROR", null, currentLocale));
        }
    }
}
