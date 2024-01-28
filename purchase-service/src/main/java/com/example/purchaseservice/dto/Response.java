package com.example.purchaseservice.dto;

public class Response<Any> {

    private Any data;
    private StatusResp status;

    public StatusResp getStatus() {
        return status;
    }

    public void setStatus(StatusResp status) {
        this.status = status;
    }

    public Any getData() {
        return data;
    }

    public void setData(Any data) {
        this.data = data;
    }
}
