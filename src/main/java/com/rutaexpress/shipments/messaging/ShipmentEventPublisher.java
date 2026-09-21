package com.rutaexpress.shipments.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rutaexpress.contracts.MessagingConstants;
import com.rutaexpress.contracts.event.ShipmentEvent;
import com.rutaexpress.shipments.domain.Shipment;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ShipmentEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ShipmentEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(Shipment shipment) {
        ShipmentEvent event = new ShipmentEvent(
                UUID.randomUUID(), shipment.getId(), shipment.getStatus(), Instant.now());
        try {
            kafkaTemplate.send(MessagingConstants.SHIPMENT_EVENTS_TOPIC,
                    objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("No se pudo serializar el evento de envío {}", shipment.getId(), e);
        }
    }
}
