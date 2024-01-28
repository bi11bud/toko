package com.example.productservice.services;

import com.example.productservice.dto.Response;
import com.example.productservice.dto.StatusResp;
import com.example.productservice.dto.content.ReportOrderHistoryResponse;
import com.example.productservice.persistance.repository.OrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    public Response<List<ReportOrderHistoryResponse>> reportOrderHistory(){

        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<List<ReportOrderHistoryResponse>> response = new Response<>();
        response.setStatus(statusResp);

        List<ReportOrderHistoryResponse> listOrderHistory = orderHistoryRepository.getOrderHistory();

        response.setData(listOrderHistory);

        return response;
    }
}
