package com.example.productservice.services;

import com.example.productservice.dto.Response;
import com.example.productservice.dto.StatusResp;
import com.example.productservice.dto.content.CategoryResponse;
import com.example.productservice.persistance.model.Category;
import com.example.productservice.persistance.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    private final static ObjectMapper mapper = new ObjectMapper();

    public Response<CategoryResponse[]> viewCategories() {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<CategoryResponse[]> response = new Response<>();
        response.setStatus(statusResp);

        List<Category> listCategoryEnt = this.categoryRepository.getAllCategories();
        List<CategoryResponse> listCategories = new ArrayList<>();

        for (Category c : listCategoryEnt) {
            CategoryResponse res = new CategoryResponse();
            res.setCategoryCode(c.getCode());
            res.setCategoryName(c.getName());
            listCategories.add(res);
        }

        CategoryResponse[] resultCategories = new CategoryResponse[listCategories.size()];
        resultCategories = listCategories.toArray(resultCategories);
        response.setData(resultCategories);

        return response;
    }

}
