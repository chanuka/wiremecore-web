package com.cba.core.wiremeweb.service;


public interface EmailService {

    void sendEmail(String userMail, String password) throws Exception;
}
