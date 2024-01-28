package com.example.purchaseservice.kafka;

import com.example.purchaseservice.persistance.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${spring.kafka.topic.name}")
    private String topicJsonName;

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Order order){

        ObjectMapper objectMapper = new ObjectMapper();
        String data = "failed";
        try {
            data = objectMapper.writeValueAsString(order);
        }catch (Exception e){
        }

        log.info(String.format("Order History sent -> %s", data));

        Message<String> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, topicJsonName)
                .build();

        kafkaTemplate.send(message);
    }
}
