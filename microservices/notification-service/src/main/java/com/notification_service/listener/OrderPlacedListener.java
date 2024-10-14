package com.notification_service.listener;

import com.notification_service.event.OrderPlacedEvent;
import com.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedListener {

    private final EmailService emailService;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        String recipient = "burcugizem956@gmail.com";
        String subject = "Yeni Sipariş Bildirimi";
        String text = "Yeni bir sipariş alındı: " + orderPlacedEvent.toString();
        log.info("mail sended..............................*****//////// order code: {} ", orderPlacedEvent.getOrderNumber());
        emailService.sendEmail(recipient, subject, text);
    }
}
