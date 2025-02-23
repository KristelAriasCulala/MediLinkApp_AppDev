package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityProduct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Find the ImageView and set an OnClickListener
        ImageView homeIcon = findViewById(R.id.home_icon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityProduct.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
            finish();
        });

    }
}

