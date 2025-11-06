package com.learnings.intern_service.event;

import com.learnings.intern_service.dto.FeedbackCreatedEvent;
import com.learnings.intern_service.entity.Feedback;
import com.learnings.intern_service.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository repository;
    private final RabbitTemplate rabbitTemplate;
    @Value("${events.exchange}")
    private String exchange;
    @Value("${events.routing.feedback-created}")
    private String routingKey;

    @Transactional
    public Feedback createFeedback(Feedback feedback) {
        Feedback saved = repository.save(feedback);

        FeedbackCreatedEvent event = new FeedbackCreatedEvent();
        event.setFeedbackId(saved.getId());
        event.setMentorId(saved.getMentorId());
        event.setMentorName(saved.getMentorName());
        event.setInternId(saved.getInternId());
        event.setMessage(saved.getMessage());
        event.setScore(saved.getScore());
        event.setCreatedAt(saved.getCreatedAt());

        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        return saved;
    }
}
