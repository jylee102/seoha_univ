//package com.seohauniv.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailUtil {
//
//    private final JavaMailSender javaMailSender;
//
//    @Autowired
//    public EmailUtil(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//    public void sendVerificationCode(String toEmail, String verificationCode) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("lilydodo123@naver.com");
//        message.setTo(toEmail);
//        message.setSubject("이메일 인증 코드");
//        message.setText("인증 코드: " + verificationCode);
//
//        javaMailSender.send(message);
//    }
//}
