package com.example.medilink_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityInventory extends AppCompatActivity {
    // First, add this Product class inside ActivityInventory.java before the main class
// (or create a separate Product.java file)
    class Product {
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

        public Uri getImageUri() { return imageUri; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getUnitPrice() { return unitPrice; }
    }
    private static final int PICK_IMAGE_REQUEST = 1;
    private DrawerLayout drawerLayout;
    private ImageView profileImage;
    private List<Product> productList = new ArrayList<>();
    private LinearLayout productContainer;
    private ImageView selectedImageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.profile_image);
        productContainer = findViewById(R.id.product_container);
        Button addButton = findViewById(R.id.add_button);

        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ActivityInventory.this, HomeActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ActivityInventory.this, ActivityProfile.class));
            } else if (id == R.id.nav_card) {
                startActivity(new Intent(ActivityInventory.this, ActivityProduct.class));
            } else if (id == R.id.nav_order) {
                startActivity(new Intent(ActivityInventory.this, ActivityInventory.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Initialize product list with two products
        // Replace the current initialization with this
        productList.add(new Product(Uri.parse("android.resource://com.example.medilink_app/" + R.drawable.imageone),
                "Paracetamol Biogesic", "Description for Paracetamol Biogesic", 50.00));
        productList.add(new Product(Uri.parse("android.resource://com.example.medilink_app/" + R.drawable.imagetwo),
                "Phenylephrine HCI", "Description for Phenylephrine HCI", 60.00));

        addButton.setOnClickListener(v -> showProductDialog(null, -1));
        updateProductList();
    }

    private void showProductDialog(Product product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_product_name);
        EditText editDescription = dialogView.findViewById(R.id.edit_product_description);
        EditText editPrice = dialogView.findViewById(R.id.edit_product_price);
        selectedImageView = dialogView.findViewById(R.id.edit_product_image);

        if (product != null) {
            editName.setText(product.getName());
            editDescription.setText(product.getDescription());
            editPrice.setText(String.valueOf(product.getUnitPrice()));
            selectedImageView.setImageURI(product.getImageUri());
        }

        selectedImageView.setOnClickListener(v -> openImagePicker());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = editName.getText().toString();
            String description = editDescription.getText().toString();
            String priceStr = editPrice.getText().toString();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                Product newProduct = new Product(selectedImageUri, name, description, price);
                if (position == -1) {
                    addProduct(newProduct);
                } else {
                    editProduct(newProduct, position);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                selectedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProduct(Product product) {
        productList.add(product);
        updateProductList();
    }

    private void editProduct(Product product, int position) {
        productList.set(position, product);
        updateProductList();
    }

    private void deleteProduct(int position) {
        productList.remove(position);
        updateProductList();
    }

    private void updateProductList() {
        productContainer.removeAllViews();
        for (int i = 0; i < productList.size(); i++) {
            final int position = i;
            Product product = productList.get(i);
            View productView = getLayoutInflater().inflate(R.layout.product_item, null);
            ImageView productImage = productView.findViewById(R.id.product_image_path);

            // Set the product image
            productImage.setImageURI(product.getImageUri());

            TextView productName = productView.findViewById(R.id.product_name);
            TextView productDescription = productView.findViewById(R.id.product_description);
            TextView productPrice = productView.findViewById(R.id.product_price);
            Button editButton = productView.findViewById(R.id.edit_button);
            Button deleteButton = productView.findViewById(R.id.delete_button);

           
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText("₱ " + product.getUnitPrice());

            editButton.setOnClickListener(v -> showProductDialog(product, position));
            deleteButton.setOnClickListener(v -> deleteProduct(position));

            productContainer.addView(productView);
        }
    }
}