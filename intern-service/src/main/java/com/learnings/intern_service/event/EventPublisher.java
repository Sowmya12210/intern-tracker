package com.learnings.intern_service.event;

import com.learnings.intern_service.dto.FeedbackCreatedEvent;

public interface EventPublisher {
    void publishFeedbackCreated(FeedbackCreatedEvent event);
}
