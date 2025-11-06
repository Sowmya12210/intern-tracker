package com.learnings.notification_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnings.notification_service.entity.Notification;
import com.learnings.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedbackEventListener {
    private final NotificationRepository repository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "notification.queue")
    public void handleFeedbackCreated(Message message) throws JsonProcessingException {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Raw payload: {}", body);
        log.info("Headers: {}", message.getMessageProperties());

        FeedbackCreatedEvent ev = objectMapper.readValue(body, FeedbackCreatedEvent.class);
        Notification n = Notification.builder()
                .feedbackId(ev.getFeedbackId())
                .toInternId(ev.getInternId())
                .fromMentorId(ev.getMentorId())
                .message("You received feedback: " + ev.getMessage())
                .delivered(false)
                .build();
        repository.save(n);
        deliverToIntern(n);
    }

    private void deliverToIntern(Notification n) {
        n.setDelivered(true);
        repository.save(n);
    }
}
