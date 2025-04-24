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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActivityInventory extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    // Use your computer's IP address or 10.0.2.2 for emulator
    private static final String BASE_URL = "http://192.168.8.41/crud/";

    private DrawerLayout drawerLayout;
    private ImageView profileImage;
    private LinearLayout productContainer;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private RequestQueue requestQueue;
    private String selectedProductId = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.profile_image);
        productContainer = findViewById(R.id.product_container);
        Button addButton = findViewById(R.id.add_button);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        addButton.setOnClickListener(v -> {
            selectedProductId = null;
            selectedImageUri = null;
            showProductDialog();
        });

        // Load initial product data
        loadProducts();
    }

    private void loadProducts() {
        productContainer.removeAllViews();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL + "fetch_products.php", null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject product = response.getJSONObject(i);
                            addProductToView(
                                    product.getString("product_id"),
                                    product.getString("product_name"),
                                    product.getString("product_description"),
                                    product.getString("product_price"),
                                    product.getString("product_image_path")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing product data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading products: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }

    private void addProductToView(String product_id, String product_name, String product_description, String product_price, String product_image_path) {
        View productView = getLayoutInflater().inflate(R.layout.product_item, productContainer, false);

        TextView tvProductId = productView.findViewById(R.id.product_id);
        ImageView ivProductImage = productView.findViewById(R.id.product_image_path);
        TextView tvProductName = productView.findViewById(R.id.product_name);
        TextView tvProductDescription = productView.findViewById(R.id.product_description);
        TextView tvProductPrice = productView.findViewById(R.id.product_price);
        Button btnEdit = productView.findViewById(R.id.edit_button);
        Button btnDelete = productView.findViewById(R.id.delete_button);

        // TODO: Implement proper image loading (consider using Picasso or Glide)
        // For now, we'll just show a placeholder if no image is available
        if (product_image_path != null && !product_image_path.isEmpty()) {
            try {
                ivProductImage.setImageURI(Uri.parse(product_image_path));
            } catch (Exception e) {
                ivProductImage.setImageResource(R.drawable.ic_placeholder);
            }
        } else {
            ivProductImage.setImageResource(R.drawable.ic_placeholder);
        }

        tvProductName.setText(product_name);
        tvProductDescription.setText(product_description);
        tvProductPrice.setText("₱ " + product_price);

        btnEdit.setOnClickListener(v -> {
            selectedProductId = product_id;
            loadProductDetails(product_id);
        });

        btnDelete.setOnClickListener(v -> {
            selectedProductId = product_id;
            deleteProduct();
        });

        productContainer.addView(productView);
    }

    private void loadProductDetails(String product_id) {
        // TODO: Implement this if you need to fetch single product details
        // For now, we'll just show the dialog with the existing data
        showProductDialog();
    }

    private void showProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        // Get references to all input fields
        EditText etName = dialogView.findViewById(R.id.edit_product_name);
        EditText etDescription = dialogView.findViewById(R.id.edit_product_description);
        EditText etPrice = dialogView.findViewById(R.id.edit_product_price);
        selectedImageView = dialogView.findViewById(R.id.edit_product_image);

        // Set up image view
        selectedImageView.setImageResource(R.drawable.ic_placeholder);
        if (selectedImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                selectedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                selectedImageView.setImageResource(R.drawable.ic_placeholder);
            }
        }

        selectedImageView.setOnClickListener(v -> openImagePicker());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get values from input fields
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            // Validate inputs
            if (name.isEmpty()) {
                etName.setError("Product name is required");
                return;
            }
            if (description.isEmpty()) {
                etDescription.setError("Description is required");
                return;
            }
            if (priceStr.isEmpty()) {
                etPrice.setError("Price is required");
                return;
            }

            // Only require image for new products
            if (selectedProductId == null && selectedImageUri == null) {
                Toast.makeText(this, "Please select an image for new products", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                String imagePath = selectedImageUri != null ? selectedImageUri.toString() : "";

                if (selectedProductId == null) {
                    createProduct(name, description, price, imagePath);
                } else {
                    updateProduct(selectedProductId, name, description, price, imagePath);
                }
            } catch (NumberFormatException e) {
                etPrice.setError("Please enter a valid price");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createProduct(String product_name, String product_description, double product_price, String product_image_path) {
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "add_products.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            loadProducts();
                        } else {
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

        };

        requestQueue.add(request);
    }

    private void updateProduct(String product_id, String product_name, String product_description, double product_price, String product_image_path) {
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "update_products.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            selectedProductId = null;
                            loadProducts();
                        } else {
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", selectedProductId); //add the id to the HashMap
                params.put("product_name", product_name);
                params.put("product_description", product_description);
                params.put("product_price", String.valueOf(product_price));
                params.put("product_image_path", product_image_path);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void deleteProduct() {
        if (selectedProductId == null) {
            Toast.makeText(this, "Please select a product to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            performDelete();
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void performDelete() {
        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "delete_products.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            selectedProductId = null;
                            loadProducts();
                        } else {
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", selectedProductId);
                return params;
            }
        };

        requestQueue.add(request);
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
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}