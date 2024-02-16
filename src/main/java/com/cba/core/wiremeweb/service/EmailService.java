package com.cba.core.wiremeweb.service;


import com.cba.core.wiremeweb.dto.TerminalEmailDto;

public interface EmailService {

    void sendEmail(String userMail, String password) throws Exception;

    void sendEmail(TerminalEmailDto terminalEmailDto) throws Exception;
}
