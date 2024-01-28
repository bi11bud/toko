package com.example.productservice.controller;

import com.example.productservice.dto.Response;
import com.example.productservice.dto.content.ProductResponse;
import com.example.productservice.dto.content.ReportOrderHistoryResponse;
import com.example.productservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping(value="/reportOrderHistory", produces = "application/json")
    public ResponseEntity<?> reportOrderHistory(){

        Response<List<ReportOrderHistoryResponse>> response = this.reportService.reportOrderHistory();

        return ResponseEntity.ok(response);
    }
}
