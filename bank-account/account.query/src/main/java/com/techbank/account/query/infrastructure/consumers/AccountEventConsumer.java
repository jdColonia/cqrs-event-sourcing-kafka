package com.techbank.account.query.infrastructure.consumers;

import com.techbank.account.query.infrastructure.handlers.EventHandler;
import com.techbank.cqrs.core.events.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AccountEventConsumer.class);
    
    @Autowired
    private EventHandler eventHandler;

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(@Payload BaseEvent event, Acknowledgment ack) {
        try {
            logger.info("🔥 CONSUMING EVENT: {} | ID: {} | Version: {} | Timestamp: {}", 
                event.getClass().getSimpleName(),
                event.getId(), 
                event.getVersion(),
                System.currentTimeMillis());
            
            var eventHandlerMethod = eventHandler.getClass().getDeclaredMethod("on", event.getClass());
            eventHandlerMethod.setAccessible(true);
            eventHandlerMethod.invoke(eventHandler, event);
            
            logger.info("✅ EVENT PROCESSED SUCCESSFULLY: {} | ID: {}", 
                event.getClass().getSimpleName(),
                event.getId());
            
            ack.acknowledge();
        } catch (Exception e) {
            logger.error("❌ ERROR CONSUMING EVENT: {} | ID: {} | Error: {}", 
                event.getClass().getSimpleName(),
                event.getId(),
                e.getMessage());
            throw new RuntimeException("Error while consuming event", e);
        }
    }
}