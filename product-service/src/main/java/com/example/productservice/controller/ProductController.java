package com.example.productservice.controller;

import com.example.productservice.dto.Response;
import com.example.productservice.dto.content.ProductRequest;
import com.example.productservice.dto.content.ProductResponse;
import com.example.productservice.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ProductController {

    @Autowired
    ProductService productService;

    private final static ObjectMapper mapper = new ObjectMapper();

    @PostMapping(value="/addProduct", produces = "application/json")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request){

        Response<ProductResponse> response = this.productService.addProduct(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/updateDataProduct", produces = "application/json")
    public ResponseEntity<?> updateDataProduct(@RequestBody ProductRequest request){

        Response<ProductResponse> response = this.productService.updateDataProduct(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/updateProduct", produces = "application/json")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest request){

        Response<ProductResponse> response = this.productService.updateProduct(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/viewProducts", produces = "application/json")
    public ResponseEntity<?> viewProducts(@RequestBody ProductRequest request){

        Response<ProductResponse[]> response = this.productService.viewProducts(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/viewAllProducts", produces = "application/json")
    public ResponseEntity<?> viewAllProducts(){

        Response<ProductResponse[]> response = this.productService.viewAllProducts();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/deleteProduct", produces = "application/json")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductRequest request){

        Response<ProductResponse> response = this.productService.deleteProduct(request);

        return ResponseEntity.ok(response);
    }
}
