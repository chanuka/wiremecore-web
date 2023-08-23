package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.dto.EmailRequestDto;
import com.cba.core.wiremeweb.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/devices/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequestDto) throws Exception {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("name", "Chanuka !");
            model.put("location", "Sri Lanka");
            model.put("sign", "Java Developer");
            model.put("type", "NEWSLETTER");
            emailRequestDto.setProps(model);

            emailService.sendEmail(emailRequestDto);
            return ResponseEntity.ok("Email sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}