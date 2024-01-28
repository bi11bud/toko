package com.example.purchaseservice.dto.content;

public class ProductResponse {

    private String productCode;
    private String productName;
    private String price;
    private String quantity;
    private String category;
    private String status;

    public ProductResponse() {
    }

    public ProductResponse(String productCode, String productName, String price, String quantity, String category, String status) {
        this.productCode = productCode;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.status = status;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
