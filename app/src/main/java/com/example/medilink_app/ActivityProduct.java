package com.example.medilink_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        // Set up click listeners for product images
        findViewById(R.id.imageView1).setOnClickListener(v -> showProductDetailsDialog(
                R.drawable.imageone, "Paracetamol Biogesic", "Description for Paracetamol Biogesic", 50.00));
        findViewById(R.id.imageView2).setOnClickListener(v -> showProductDetailsDialog(
                R.drawable.imagetwo, "Phenylephrine HCI", "Description for Phenylephrine HCI", 60.00));
        findViewById(R.id.imageView3).setOnClickListener(v -> showProductDetailsDialog(
                R.drawable.imagethree, "Ibuprofen Medicol", "Description for Ibuprofen Medicol", 70.00));
        findViewById(R.id.imageView4).setOnClickListener(v -> showProductDetailsDialog(
                R.drawable.imagefour, "Cetirizine HCI", "Description for Cetirizine HCI", 80.00));
        findViewById(R.id.imageView5).setOnClickListener(v -> showProductDetailsDialog(
                R.drawable.imagefive, "Loratadine", "Description for Loratadine", 90.00));
        findViewById(R.id.imageView6).setOnClickListener(v -> showProductDetailsDialog(
                R.drawable.imagesix, "Guardium", "Description for Guardium", 100.00));
    }

    private void showProductDetailsDialog(int imageResId, String name, String description, double unitPrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_details, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set up dialog views and actions
        ImageView productImage = dialogView.findViewById(R.id.product_image);
        TextView productName = dialogView.findViewById(R.id.product_name);
        TextView productDescription = dialogView.findViewById(R.id.product_description);
        TextView productPrice = dialogView.findViewById(R.id.product_price);
        TextView txtQuantity = dialogView.findViewById(R.id.txt_quantity);
        Button btnDecrease = dialogView.findViewById(R.id.btn_decrease);
        Button btnIncrease = dialogView.findViewById(R.id.btn_increase);
        ImageView closeButton = dialogView.findViewById(R.id.btn_close);

        productImage.setImageResource(imageResId);
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText("₱ " + unitPrice);

        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
                productPrice.setText("₱ " + (unitPrice * quantity));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
            productPrice.setText("₱ " + (unitPrice * quantity));
        });

        closeButton.setOnClickListener(v -> dialog.dismiss());
    }
}