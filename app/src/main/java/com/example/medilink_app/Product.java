package com.example.medilink_app;

public class Product {
    private int productId;
    private String name;
    private String description;
    private int stock;
    private double price;
    private String unit;

    public Product(int productId, String name, String description, int stock, double price, String unit) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.unit = unit;
    }

    // Getters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getStock() { return stock; }
    public double getPrice() { return price; }
    public String getUnit() { return unit; }
}