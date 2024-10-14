package com.notification_service.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
