package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.dto.EmailRequestDto;
import com.cba.core.wiremeweb.exception.InternalServerError;
import com.cba.core.wiremeweb.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/devices/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequestDto) {
        try {

//            Map<String, Object> model = new HashMap<>();
//            model.put("name", "Chanuka !");
//            model.put("location", "Sri Lanka");
//            model.put("sign", "Java Developer");
//            model.put("type", "NEWSLETTER");
//            emailRequestDto.setProps(model);
//
//            emailService.sendEmail(emailRequestDto);
//            return ResponseEntity.ok("Email sent successfully");

            List<PermissionResponseDto> permissionDtoList = new ArrayList<>();
            permissionDtoList.add(new PermissionResponseDto("resource1", true, false, false, false));
            permissionDtoList.add(new PermissionResponseDto("resource2", false, true, true, false));
            permissionDtoList.add(new PermissionResponseDto("resource3", false, false, false, true));

            String resource = emailRequestDto.getMailTo();
            String method =  emailRequestDto.getSubject();

            boolean access = permissionDtoList.stream()
                    .filter(permission -> permission.getResource().equals(resource))
                    .anyMatch(permission -> (
                            (method.equals("GET") && permission.isReadd()) ||
                                    (method.equals("POST") && permission.isCreated()) ||
                                    (method.equals("PUT") && permission.isUpdated()) ||
                                    (method.equals("DELETE") && permission.isDeleted())
                    ));

            if (access) {
                System.out.println("Access granted!");
            } else {
                System.out.println("Access denied.");
            }

            return ResponseEntity.ok("Email sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError("Internal Server Error has Occurred");
        }
    }
}


class PermissionResponseDto {
    private String resource;
    private boolean readd;
    private boolean created;
    private boolean updated;
    private boolean deleted;

    public PermissionResponseDto(String resource, boolean readd, boolean created, boolean updated, boolean deleted) {
        this.resource = resource;
        this.readd = readd;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    public String getResource() {
        return resource;
    }

    public boolean isReadd() {
        return readd;
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isUpdated() {
        return updated;
    }

    public boolean isDeleted() {
        return deleted;
    }
}