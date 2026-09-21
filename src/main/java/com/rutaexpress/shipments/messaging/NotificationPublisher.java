package com.rutaexpress.shipments.messaging;

import com.rutaexpress.contracts.MessagingConstants;
import com.rutaexpress.contracts.event.NotificationRequest;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String channel, String recipient, String subject, String body) {
        NotificationRequest request = new NotificationRequest(
                UUID.randomUUID(), channel, recipient, subject, body);
        try {
            rabbitTemplate.convertAndSend(MessagingConstants.NOTIFICATIONS_QUEUE, request);
        } catch (AmqpException e) {
            log.error("No se pudo publicar la notificación para {}", recipient, e);
        }
    }
}
