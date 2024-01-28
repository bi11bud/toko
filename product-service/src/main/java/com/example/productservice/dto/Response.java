package com.example.productservice.dto;

public class Response<Any> {
    private StatusResp statusResp;
    private Any data;

    public StatusResp getStatus() {
        return statusResp;
    }

    public void setStatus(StatusResp statusResp) {
        this.statusResp = statusResp;
    }

    public Any getData() {
        return data;
    }

    public void setData(Any data) {
        this.data = data;
    }
}
