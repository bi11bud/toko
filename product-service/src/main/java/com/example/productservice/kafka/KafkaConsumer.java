package com.example.productservice.kafka;

import com.example.productservice.dto.content.OrderResponse;
import com.example.productservice.persistance.model.OrderHistory;
import com.example.productservice.persistance.repository.OrderHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String response) throws Exception {
        log.info(String.format("Json message recieved -> %s", response));

        if (!response.equalsIgnoreCase("failed")) {

            ObjectMapper objectMapper = new ObjectMapper();
            OrderResponse orderResponse = objectMapper.readValue(response, OrderResponse.class);

            try {
                log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderResponse));
            } catch (Exception e) {
                log.error("error," + e);
            }

            OrderHistory order = new OrderHistory();
            order.setOrderId(orderResponse.getOrderId());
            order.setProductCode(orderResponse.getProductCode());
            order.setProductName(orderResponse.getProductName());
            order.setPrice(orderResponse.getPrice());
            order.setQuantity(orderResponse.getQuantity());
            order.setOrderBy(orderResponse.getOrderBy());
            order.setCby(orderResponse.getCby());
            order.setCdate(orderResponse.getCdate());
            order.setMby(orderResponse.getMby());
            order.setMdate(orderResponse.getMdate());
            orderHistoryRepository.save(order);

            log.info("Stored Order History");
        }

    }
}
