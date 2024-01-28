package com.example.purchaseservice.controller;

import com.example.purchaseservice.dto.Response;
import com.example.purchaseservice.dto.content.OrderRequest;
import com.example.purchaseservice.dto.content.OrderResponse;
import com.example.purchaseservice.service.OrderHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class OrderController {

    @Autowired
    OrderHandler orderHandler;

    @ResponseStatus
    @PostMapping(value = "/orderProduct", produces = "application/json")
    public ResponseEntity<?> orderProduct(@RequestBody OrderRequest request) throws JsonProcessingException {

        Response<OrderResponse> response = this.orderHandler.orderProduct(request);

        return ResponseEntity.ok(response);
    }

    @ResponseStatus
    @PostMapping(value = "/viewOrder", produces = "application/json")
    public ResponseEntity<?> viewOrder(@RequestBody OrderRequest request) {

        Response<OrderResponse[]> response = this.orderHandler.viewOrder(request);

        return ResponseEntity.ok(response);
    }

}
