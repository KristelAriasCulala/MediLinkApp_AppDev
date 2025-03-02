package com.example.medilink_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ViewPager2 imageCarousel;
    private DrawerLayout drawerLayout; // DrawerLayout reference
    private ImageView profileImage; // ImageView to open Drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button exploreButton = findViewById(R.id.explore_button);

        exploreButton.setOnClickListener(v -> {
            // Show modal (AlertDialog)
            showModal();
        });

        // Set background color programmatically
        exploreButton.getBackground().setTintList(null); // Remove any applied tint
        exploreButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        exploreButton.setBackgroundResource(R.drawable.rounded_button_black);

        // Initialize ViewPager2
        imageCarousel = findViewById(R.id.imageCarousel);

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initialize ImageView to open Drawer
        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START)); // Open Drawer when clicked

        // Set up image list for the carousel
        List<Integer> images = Arrays.asList(
                R.drawable.imageone,
                R.drawable.imagetwo
        );

        // Set up the adapter
        ImageAdapter adapter = new ImageAdapter(this, images);
        imageCarousel.setAdapter(adapter);

        // Handle Navigation Drawer Item Clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ActivityProfile.class));
            } else if (id == R.id.nav_product) {
                startActivity(new Intent(HomeActivity.this, ActivityProduct.class));
            }
            drawerLayout.closeDrawer(Gravity.START); // Close Drawer after click
            return true;
        });
    }
    private void showModal() {
        // Create Dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_dialog);

        // Find dialog elements
        ImageView closeDialog = bottomSheetDialog.findViewById(R.id.close_dialog);
        Button okButton = bottomSheetDialog.findViewById(R.id.ok_button);
        TextView learnMore = bottomSheetDialog.findViewById(R.id.learn_more);

        // Close button functionality
        closeDialog.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // OK button functionality
        okButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Learn more functionality (you can open a new activity or URL)
        learnMore.setOnClickListener(v -> {
            // Example: Open a webpage
            // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourwebsite.com"));
            // startActivity(intent);
        });

        // Show bottom sheet
        bottomSheetDialog.show();
    }
}
