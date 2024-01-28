package com.example.productservice.services;

import com.example.productservice.dto.Response;
import com.example.productservice.dto.StatusResp;
import com.example.productservice.dto.content.ProductRequest;
import com.example.productservice.dto.content.ProductResponse;
import com.example.productservice.persistance.model.Category;
import com.example.productservice.persistance.model.Product;
import com.example.productservice.persistance.model.Status;
import com.example.productservice.persistance.repository.CategoryRepository;
import com.example.productservice.persistance.repository.ProductRepository;
import com.example.productservice.persistance.repository.StatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    StatusRepository statusRepository;

    private final static ObjectMapper mapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    public Response<ProductResponse> addProduct(ProductRequest request) {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<ProductResponse> response = new Response<>();
        response.setStatus(statusResp);
        response.setData(null);

        try {
            log.info("Request: \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        } catch (Exception e) {
        }

        if (request == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Invalid Request");
            response.setStatus(statusResp);
            return response;
        }

        Product checkCode = this.productRepository.findByProductCode(request.getProductCode());
        if (checkCode != null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Product Code Already Exist");
            response.setStatus(statusResp);
            return response;
        }

        Product checkName = this.productRepository.findByProductName(request.getProductName().toUpperCase());
        if (checkName != null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Product Name Already Exist");
            response.setStatus(statusResp);
            return response;
        }

        Category category = this.categoryRepository.findByCategoryCode(request.getCategory());
        if (category == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Invalid Category Name");
            response.setStatus(statusResp);
            return response;
        }

        Status status = this.statusRepository.findByTypeAndCode("product", "active");
        if (status == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Invalid Status Name");
            response.setStatus(statusResp);
            return response;
        }

        Date now = new Date();

        Product product = new Product();
        product.setCode(request.getProductCode());
        product.setName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(category);
        product.setStatus(status);
        product.setCby("admin");
        product.setCdate(now);
        product.setMby("admin");
        product.setMdate(now);

        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error("Save Product Failed: \n", e);
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Save Failed");
            response.setStatus(statusResp);
            return response;
        }

        log.info("Done Add Product");

        return response;
    }

    public Response<ProductResponse> updateDataProduct(ProductRequest request) {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<ProductResponse> response = new Response<>();
        response.setStatus(statusResp);

        Product p = this.productRepository.findByProductCode(request.getProductCode());
        if (p == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Product Code not found");
            response.setStatus(statusResp);
            return response;
        }

        Product checkName = this.productRepository.findByProductName(request.getProductName().toUpperCase());
        if (checkName != null) {
            if (!checkName.getCode().equalsIgnoreCase(p.getCode())) {
                statusResp.setStatusCode("9999");
                statusResp.setStatusDesc("Product Name Already Exist");
                response.setStatus(statusResp);
                return response;
            }
        }

        if(request.getStatus().equalsIgnoreCase("Y")){
            request.setStatus("active");
        } else if (request.getStatus().equalsIgnoreCase("N")){
            request.setStatus("inactive");
        } else {
            log.info("status: " + request.getStatus());
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Invalid Status Product Request");
            response.setStatus(statusResp);
            return response;
        }

        Status status = this.statusRepository.findByTypeAndCode("product", request.getStatus());
        if (status == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Status not Found");
            response.setStatus(statusResp);
            return response;
        }

        Category category = this.categoryRepository.findByCategoryName(request.getCategory());
        if (category == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Category Name Not Found");
            response.setStatus(statusResp);
            return response;
        }

        Date now = new Date();

        p.setName(request.getProductName());
        p.setQuantity(request.getQuantity());
        p.setPrice(request.getPrice());
        p.setStatus(status);
        p.setCategory(category);
        p.setMdate(now);
        p.setMby("admin");
        try {
            productRepository.save(p);
        } catch (Exception e) {
            log.error("Update Product Failed: \n", e);
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Update Failed");
            response.setStatus(statusResp);
            return response;
        }

        ProductResponse res = new ProductResponse();
        res.setProductCode(p.getCode());
        res.setProductName(p.getName());
        res.setQuantity(p.getQuantity());
        res.setCategory(p.getCategory().getName());
        res.setPrice(p.getPrice());
        res.setStatus(p.getStatus().getName());

        response.setData(res);

        log.info("Done Update Data Product");

        return response;
    }

    public Response<ProductResponse> updateProduct(ProductRequest request) {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<ProductResponse> response = new Response<>();
        response.setStatus(statusResp);

        Product p = this.productRepository.findByProductCode(request.getProductCode());
        if (p == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Product Code not found");
            response.setStatus(statusResp);
            return response;
        }

        Date now = new Date();

        p.setQuantity(request.getQuantity());
        p.setMdate(now);
        p.setMby("admin");
        try {
            productRepository.save(p);
        } catch (Exception e) {
            log.error("Update Product Failed: \n", e);
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Update Failed");
            response.setStatus(statusResp);
            return response;
        }

        ProductResponse res = new ProductResponse();
        res.setProductCode(p.getCode());
        res.setProductName(p.getName());
        res.setQuantity(p.getQuantity());
        res.setPrice(p.getPrice());
        res.setCategory(p.getCategory().getName());
        res.setStatus(p.getStatus().getName());

        response.setData(res);

        log.info("Done Update Product");

        return response;
    }

    public Response<ProductResponse[]> viewProducts(ProductRequest request) {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<ProductResponse[]> response = new Response<>();
        response.setStatus(statusResp);

        List<Product> listProductEnt = new ArrayList<>();
        List<ProductResponse> listProduct = new ArrayList<>();

        if (request.getProductCode() == null || request.getProductCode() == "") {
            listProductEnt = this.productRepository.getAllActiveProducts();
        } else {
            Product getProduct = this.productRepository.findByProductCode(request.getProductCode());
            if (getProduct == null) {
                statusResp.setStatusCode("9999");
                statusResp.setStatusDesc("Product Code not found");
                response.setStatus(statusResp);
                return response;
            }
            listProductEnt.add(getProduct);
        }

        try {
            log.info("List Product:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listProductEnt));
        } catch (Exception ex) {
        }

        for (Product prod : listProductEnt) {
            ProductResponse p = new ProductResponse();
            p.setProductCode(prod.getCode());
            p.setProductName(prod.getName());
            p.setPrice(prod.getPrice());
            p.setQuantity(prod.getQuantity());
            p.setCategory(prod.getCategory().getName());
            p.setStatus(prod.getStatus().getName());
            listProduct.add(p);
        }

        ProductResponse[] resultProduct = new ProductResponse[listProduct.size()];
        resultProduct = listProduct.toArray(resultProduct);
        response.setData(resultProduct);

        log.info("Done View Product");

        return response;
    }

    public Response<ProductResponse[]> viewAllProducts() {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<ProductResponse[]> response = new Response<>();
        response.setStatus(statusResp);

        List<Product> listProductEnt = this.productRepository.getAllProducts();
        List<ProductResponse> listProduct = new ArrayList<>();

        try {
            log.info("List Product:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listProductEnt));
        } catch (Exception ex) {
        }

        for (Product prod : listProductEnt) {
            ProductResponse p = new ProductResponse();
            p.setProductCode(prod.getCode());
            p.setProductName(prod.getName());
            p.setPrice(prod.getPrice());
            p.setQuantity(prod.getQuantity());
            p.setCategory(prod.getCategory().getName());
            p.setStatus(prod.getStatus().getName());
            listProduct.add(p);
        }

        ProductResponse[] resultProduct = new ProductResponse[listProduct.size()];
        resultProduct = listProduct.toArray(resultProduct);
        response.setData(resultProduct);

        log.info("Done View All Products");

        return response;
    }

    public Response<ProductResponse> deleteProduct(ProductRequest request) {
        StatusResp statusResp = new StatusResp();
        statusResp.setStatusCode("0000");
        statusResp.setStatusDesc("OK");

        Response<ProductResponse> response = new Response<>();
        response.setStatus(statusResp);

        try {
            log.info("Request:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        } catch (Exception ex) {
        }

        Product product = this.productRepository.findByProductCode(request.getProductCode());
        if (product == null) {
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Product Code not found");
            response.setStatus(statusResp);
            return response;
        }

        try {
            this.productRepository.delete(product);
        } catch (Exception e) {
            log.error("Delete Product Failed: \n", e);
            statusResp.setStatusCode("9999");
            statusResp.setStatusDesc("Delete Failed");
            response.setStatus(statusResp);
            return response;
        }

        log.info("Done Delete Product");

        return response;
    }

}
