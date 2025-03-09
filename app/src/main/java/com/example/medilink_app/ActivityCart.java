package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityCart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView cartProductName = findViewById(R.id.cart_product_name);
        TextView cartProductPrice = findViewById(R.id.cart_product_price);
        TextView cartProductQuantity = findViewById(R.id.cart_product_quantity);
        Button btnCheckout = findViewById(R.id.btn_checkout); // Checkout button

        // Get data from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        double price = intent.getDoubleExtra("product_price", 0);
        int quantity = intent.getIntExtra("quantity", 1);

        // Set the data to views
        cartProductName.setText(name);
        cartProductPrice.setText("₱ " + (price * quantity));
        cartProductQuantity.setText("Quantity: " + quantity);

        // Handle Checkout Button Click
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(ActivityCart.this, ActivityPayment.class);
                startActivity(paymentIntent); // Navigate to ActivityPayment
            }
        });
    }
}
