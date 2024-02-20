package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dto.EmailRequestDto;
import com.cba.core.wiremeweb.dto.TerminalEmailDto;
import com.cba.core.wiremeweb.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.email.url}")
    private String emailUrl;
    private final RestTemplate restTemplate;


    @Override
    @Async("asyncExecutor")
    public void sendEmail(String userMail, String messageBody) throws Exception {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setTo(userMail);
            emailRequestDto.setBody(messageBody);
            emailRequestDto.setSubject("Wire-me user credentials/OTP(confidencial)");
            emailRequestDto.setIsHtml(false);

            HttpEntity<EmailRequestDto> requestEntity = new HttpEntity<>(emailRequestDto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(emailUrl+ "/email", requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseData = response.getBody();
                // Process responseData here
                System.out.println("Response: " + responseData);
            } else {
                System.out.println("Error occurred. Status code: " + response.getStatusCodeValue());
                // Handle other error cases
            }
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
            // Handle exceptions
        }
    }

    @Override
    @Async("asyncExecutor")
    public void sendEmail(TerminalEmailDto terminalEmailDto) throws Exception {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TerminalEmailDto> requestEntity = new HttpEntity<>(terminalEmailDto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(emailUrl+ "/terminal-email", requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseData = response.getBody();
                // Process responseData here
                System.out.println("Response: " + responseData);
            } else {
                System.out.println("Error occurred. Status code: " + response.getStatusCodeValue());
                // Handle other error cases
            }
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
            // Handle exceptions
        }
    }
}
