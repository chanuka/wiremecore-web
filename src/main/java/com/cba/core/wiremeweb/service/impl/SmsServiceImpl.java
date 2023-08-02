package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.service.SmsService;

public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String to, String message) {
        System.out.println("SMS has been sent successfully");
    }
}
