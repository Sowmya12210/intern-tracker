package com.learnings.intern_service.event;

import com.learnings.intern_service.dto.FeedbackCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    @Value("${events.exchange}") private String exchange;
    @Value("${events.routing.feedback-created}") private String routingKey;

    @Override
    public void publishFeedbackCreated(FeedbackCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}

