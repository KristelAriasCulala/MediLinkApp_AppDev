package com.example.medilink_app;

public class Order {
    private String orderId;
    private String date;
    private String status;
    private String totalAmount;

    public Order(String orderId, String date, String status, String totalAmount) {
        this.orderId = orderId;
        this.date = date;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}
