package com.example.medilink_app;

public class Order {
    private int orderId;
    private String orderDate;
    private double totalAmount;
    private String orderStatus;

    public Order(int orderId, String orderDate, double totalAmount, String orderStatus) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}