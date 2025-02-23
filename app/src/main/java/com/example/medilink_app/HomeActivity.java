package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    ImageView shoppingIcon; // Declare shoppingIcon as a class member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize shoppingIcon AFTER setContentView
        shoppingIcon = findViewById(R.id.shopping_icon);

        // Set the OnClickListener
        shoppingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ActivityProduct.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });
    }
}