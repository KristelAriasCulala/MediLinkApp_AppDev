package com.example.medilink_app;

public class CartItem {
    private int orderItemId;
    private int productId;
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private double subtotal;

    public CartItem(int orderItemId, int productId, String productName, int quantity, double pricePerUnit, double subtotal) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = subtotal;
    }

    // Getters
    public int getOrderItemId() { return orderItemId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getSubtotal() { return subtotal; }
}