//package com.cba.core.wiremeweb.service.impl;
//
//import com.cba.core.wiremeweb.dto.EmailRequestDto;
//import com.cba.core.wiremeweb.service.EmailService;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Service
//@RequiredArgsConstructor
//public class EmailServiceImpl_back implements EmailService {
//
//    private final JavaMailSender javaMailSender;
//
//    private final SpringTemplateEngine templateEngine;
//
//
//    @Override
//    @Async("asyncExecutor")
//    public void sendEmail(EmailRequestDto emailRequestDto) throws MessagingException, IOException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message,
//                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                StandardCharsets.UTF_8.name());
//
//        helper.addAttachment("template-cover.png", new ClassPathResource("static/javabydeveloper-email.PNG"));
//
//        Context context = new Context();
//        context.setVariables(emailRequestDto.getProps());
//
//        final String template = emailRequestDto.getProps().get("type").equals("NEWSLETTER") ? "newsletter-template" : "inlined-css-template";
//        String html = templateEngine.process(template, context);
//
//        helper.setTo(emailRequestDto.getMailTo());
//        helper.setText(html, true);
//        helper.setSubject(emailRequestDto.getSubject());
////        helper.setFrom(emailRequestDto.getFrom());
//
//        javaMailSender.send(message);
//    }
//
//    @Override
//    @Async("asyncExecutor")
//    public void sendEmail(String userMail, String messageBody) throws MessagingException, IOException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message,
//                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                StandardCharsets.UTF_8.name());
//
//        helper.setTo(userMail);
//        helper.setText(messageBody);
//        helper.setSubject("This is auto generated email - include credentials/OTP");
////        helper.setFrom(emailRequestDto.getFrom());
//
//        javaMailSender.send(message);
//    }
//}
