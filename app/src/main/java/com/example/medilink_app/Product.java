package com.example.medilink_app;
import android.net.Uri;

public class Product {
    private Uri imageUri;
    private String name;
    private String description;
    private double unitPrice;

    public Product(Uri imageUri, String name, String description, double unitPrice) {
        this.imageUri = imageUri;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}