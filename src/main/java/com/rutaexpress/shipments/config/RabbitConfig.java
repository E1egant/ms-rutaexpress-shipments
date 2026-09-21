package com.rutaexpress.shipments.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rutaexpress.contracts.MessagingConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    Queue notificationsQueue() {
        return QueueBuilder.durable(MessagingConstants.NOTIFICATIONS_QUEUE).build();
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
