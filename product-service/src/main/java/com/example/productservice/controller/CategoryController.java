package com.example.productservice.controller;

import com.example.productservice.dto.Response;
import com.example.productservice.dto.content.CategoryRequest;
import com.example.productservice.dto.content.CategoryResponse;
import com.example.productservice.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    private final static ObjectMapper mapper = new ObjectMapper();

    @GetMapping(value="/viewCategories", produces = "application/json")
    public ResponseEntity<?> viewCategories(){

        Response<CategoryResponse[]> response = this.categoryService.viewCategories();

        return ResponseEntity.ok(response);
    }
}
