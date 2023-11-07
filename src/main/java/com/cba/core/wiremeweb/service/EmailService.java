package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.EmailRequestDto;
import com.cba.core.wiremeweb.model.User;

public interface EmailService {
    void sendEmail(EmailRequestDto emailRequestDto) throws Exception;

    void sendEmail(String userMail, String password) throws Exception;
}
