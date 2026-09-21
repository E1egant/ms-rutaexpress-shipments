package com.rutaexpress.contracts.event;

import java.util.UUID;

/**
 * Solicitud de notificación publicada en RabbitMQ (cola de trabajo).
 * La consume el servicio de notificaciones.
 */
public record NotificationRequest(
        UUID id,
        String channel,
        String recipient,
        String subject,
        String body) {
}
