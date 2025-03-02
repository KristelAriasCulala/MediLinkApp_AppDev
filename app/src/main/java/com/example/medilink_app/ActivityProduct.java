package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class ActivityProduct extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product); // Correct layout for this activity

        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.profile_image);

        // Open drawer when profile image is clicked
        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));

        // Handle Navigation Drawer Item Clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ActivityProduct.this, HomeActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ActivityProduct.this, ActivityProfile.class));
            } else if (id == R.id.nav_product) {
                startActivity(new Intent(ActivityProduct.this, ActivityProduct.class));
            }
            drawerLayout.closeDrawer(Gravity.START); // Close drawer after click
            return true;
        });
    }
}
