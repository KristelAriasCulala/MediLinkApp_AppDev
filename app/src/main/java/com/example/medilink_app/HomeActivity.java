package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ImageView shoppingIcon; // Declare shoppingIcon as a class member
    ViewPager2 imageCarousel; // Declare ViewPager2 as a class member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize shoppingIcon AFTER setContentView
        shoppingIcon = findViewById(R.id.shopping_icon);
        imageCarousel = findViewById(R.id.imageCarousel); // Initialize ViewPager2

        // Set the OnClickListener for shopping icon
        shoppingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ActivityProduct.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        // List of image resources for carousel
        List<Integer> images = Arrays.asList(
                R.drawable.imageone,
                R.drawable.imagetwo
        );

        ImageAdapter adapter = new ImageAdapter(this, images);
        imageCarousel.setAdapter(adapter);
    }
}
