package com.example.productservice.dto.content;

import java.util.Date;

public class ReportOrderHistoryResponse {

    private String orderId;
    private String orderBy;
    private String productCode;
    private String productName;
    private String categoryName;
    private String price;
    private String quantity;
    private Date cdate;
    private String cby;
    private Date mdate;
    private String mby;

    public ReportOrderHistoryResponse(String orderId, String orderBy, String productCode, String productName, String price, String quantity, Date cdate, String cby, Date mdate, String mby, String categoryName) {
        this.orderId = orderId;
        this.orderBy = orderBy;
        this.productCode = productCode;
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
        this.quantity = quantity;
        this.cdate = cdate;
        this.cby = cby;
        this.mdate = mdate;
        this.mby = mby;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getCby() {
        return cby;
    }

    public void setCby(String cby) {
        this.cby = cby;
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public String getMby() {
        return mby;
    }

    public void setMby(String mby) {
        this.mby = mby;
    }
}
