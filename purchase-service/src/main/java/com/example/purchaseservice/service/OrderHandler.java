package com.example.purchaseservice.service;

import com.example.purchaseservice.dto.Response;
import com.example.purchaseservice.dto.StatusResp;
import com.example.purchaseservice.dto.content.*;
import com.example.purchaseservice.kafka.KafkaProducer;
import com.example.purchaseservice.persistance.model.Order;
import com.example.purchaseservice.persistance.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderHandler {

    @Autowired
    OrderRepository orderRepository;

    private final static ObjectMapper mapper = new ObjectMapper();

    private Logger log = LoggerFactory.getLogger(OrderHandler.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KafkaProducer kafkaProducer;

    @Value("${product_service_url}")
    private String productServiceUrl;

    public Response<OrderResponse> orderProduct(OrderRequest request) throws JsonProcessingException {
        StatusResp status = new StatusResp();
        status.setStatusCode("0000");
        status.setStatusDesc("OK");

        Response<OrderResponse> response = new Response<>();
        response.setStatus(status);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ProductRequest reqParam = new ProductRequest();
        reqParam.setProductCode(request.getProductCode());

        HttpEntity<ProductRequest> req = new HttpEntity<>(reqParam, headers);
        ResponseEntity<Response<ProductResponse[]>> res = restTemplate.exchange(
                productServiceUrl + "/viewProducts",
                HttpMethod.POST,
                req,
                new ParameterizedTypeReference<Response<ProductResponse[]>>() {}
        );

        if(!res.getBody().getStatus().getStatusCode().equals("0000")){
            status.setStatusCode("9999");
            status.setStatusDesc(res.getBody().getStatus().getStatusDesc());
            response.setStatus(status);
            return response;
        }

        ProductResponse product = res.getBody().getData()[0];
        if (product == null) {
            status.setStatusCode("9999");
            status.setStatusDesc("Product not found");
            response.setStatus(status);
            return response;
        }

        int validQuantity = Integer.parseInt(product.getQuantity()) - Integer.parseInt(request.getQuantity());

        log.info("sisa quantity: " + validQuantity);
        if (validQuantity < 0) {
            status.setStatusCode("9999");
            status.setStatusDesc("Invalid Amount of Quantity");
            response.setStatus(status);
            return response;
        }

        ProductUpdateRequest reqUpdateProduct = new ProductUpdateRequest();
        reqUpdateProduct.setProductCode(request.getProductCode());
        reqUpdateProduct.setQuantity(request.getQuantity());

        HttpEntity<ProductUpdateRequest> reqUpdate = new HttpEntity<>(reqUpdateProduct, headers);
        ResponseEntity<Response<ProductResponse>> resUpdate = restTemplate.exchange(
                productServiceUrl+"/updateProduct",
                HttpMethod.POST,
                reqUpdate,
                new ParameterizedTypeReference<Response<ProductResponse>>(){});

        if(!resUpdate.getBody().getStatus().getStatusCode().equals("0000")){
            status.setStatusCode("9999");
            status.setStatusDesc(resUpdate.getBody().getStatus().getStatusDesc());
            response.setStatus(status);
            return response;
        }

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy-HHmmss");
        String date = sdf.format(now);

        Order orderEnt = new Order();
        orderEnt.setOrderId(product.getProductCode()+"-"+date);
        orderEnt.setProductCode(product.getProductCode());
        orderEnt.setProductName(product.getProductName());
        orderEnt.setPrice(product.getPrice());
        orderEnt.setQuantity(request.getQuantity());
        orderEnt.setOrderBy(request.getOrderBy());
        orderEnt.setCby("user");
        orderEnt.setCdate(now);
        orderEnt.setMby("user");
        orderEnt.setMdate(now);

        try {
            log.info("Order: \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderEnt));
            log.info("save order");
            orderRepository.save(orderEnt);
        } catch (Exception e) {
            log.error("Failed save to database");
            status.setStatusCode("9999");
            status.setStatusDesc("Failed Save Order");
            response.setStatus(status);
            return response;
        }

        OrderResponse resData = new OrderResponse();
        resData.setOrderId(orderEnt.getOrderId());
        resData.setProductCode(product.getProductCode());
        resData.setOrderBy(orderEnt.getOrderBy());
        resData.setProductName(product.getProductName());
        resData.setPrice(product.getPrice());
        resData.setQuantity(orderEnt.getQuantity());
        int totalPrice = Integer.parseInt(orderEnt.getPrice()) * Integer.parseInt(orderEnt.getQuantity());
        resData.setTotalPrice(String.valueOf(totalPrice));

        response.setData(resData);

        log.info("Done Purchase Product");
        kafkaProducer.sendMessage(orderEnt);
        log.info("Done Send Order Response Message");

        return response;
    }

    public Response<OrderResponse[]> viewOrder(OrderRequest request) {
        StatusResp status = new StatusResp();
        status.setStatusCode("0000");
        status.setStatusDesc("OK");

        Response<OrderResponse[]> response = new Response<>();
        response.setStatus(status);

        List<Order> listOrderEnt = new ArrayList<>();
        List<OrderResponse> listOrder = new ArrayList<>();

        if ((request.getOrderId() != null && !request.getOrderId().equals(""))
                && (request.getProductCode() == null || request.getProductCode().equals(""))
                && (request.getOrderBy() == null || request.getOrderBy().equals(""))) {
            Order order = this.orderRepository.findByOrderId(request.getOrderId());
            if (order != null) {
                listOrderEnt.add(order);
            }
        } else if ((request.getOrderId() == null || request.getOrderId().equals(""))
                && (request.getProductCode() != null && !request.getProductCode().equals(""))
                && (request.getOrderBy() == null || request.getOrderBy().equals(""))) {
            listOrderEnt = this.orderRepository.findByProductCode(request.getProductCode());
        } else if ((request.getOrderId() == null || request.getOrderId().equals(""))
                && (request.getProductCode() == null || request.getProductCode().equals(""))
                && (request.getOrderBy() != null && !request.getOrderBy().equals(""))) {
            listOrderEnt = this.orderRepository.findAllByOrderByIgnoreCaseContaining(request.getOrderBy().toUpperCase());
        } else if ((request.getOrderId() == null || request.getOrderId().equals(""))
                && (request.getProductCode() != null || !request.getProductCode().equals(""))
                && (request.getOrderBy() != null && !request.getOrderBy().equals(""))) {
            listOrderEnt = this.orderRepository.findAllByProductCodeAndOrderByIgnoreCaseContaining(request.getProductCode(), request.getOrderBy().toUpperCase());
        }else {
            listOrderEnt = this.orderRepository.findAllByOrderByCdateAsc();
        }


        try {
            log.info("List Order:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listOrderEnt));
        } catch (Exception ex) {
        }

        for (Order order : listOrderEnt) {
            OrderResponse o = new OrderResponse();
            o.setOrderId(order.getOrderId());
            o.setProductCode(order.getProductCode());
            o.setOrderBy(order.getOrderBy());
            o.setProductName(order.getProductName());
            o.setPrice(order.getPrice());
            o.setQuantity(order.getQuantity());
            int totalPrice = Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity());
            o.setTotalPrice(String.valueOf(totalPrice));
            listOrder.add(o);
        }

        OrderResponse[] resultOrder = new OrderResponse[listOrder.size()];
        resultOrder = listOrder.toArray(resultOrder);
        response.setData(resultOrder);

        return response;
    }
}
