package com.example.medilink_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String PRODUCTS_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/products.php";
    private static final String USER_PROFILE_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/userprofile.php";

    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private ViewPager2 imageCarousel;
    private DrawerLayout drawerLayout; // DrawerLayout reference
    private ImageView profileImage; // ImageView to open Drawer
    @Override
    protected void onResume() {
        super.onResume();
        // Update nav header with user info when the activity resumes
        updateNavHeader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);

        // Replace this line:
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

// With this:
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productsRecyclerView.setLayoutManager(linearLayoutManager);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        productsRecyclerView.setAdapter(productAdapter);
        fetchProducts();


        // Initialize ViewPager2
        imageCarousel = findViewById(R.id.imageCarousel);

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initialize ImageView to open Drawer
        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.LEFT)); // Open Drawer when clicked

        // Set up image list for the carousel
        List<Integer> images = Arrays.asList(
                R.drawable.carousel1,
                R.drawable.carousel2,
                R.drawable.carousel3
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
            } else if (id == R.id.nav_card) {
                startActivity(new Intent(HomeActivity.this, ActivityCart.class));
            }  else if (id == R.id.nav_order) {
                startActivity(new Intent(HomeActivity.this, EprescriptionActivity.class));
            }else if (id == R.id.order_history) {
            startActivity(new Intent(HomeActivity.this, ActivityOrderHistory.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START); // Close Drawer after click
            return true;
        });


        // Initialize ImageView and set OnClickListener



    }
    private void updateNavHeader() {
        // Get user ID from SharedPreferences (you should save this during login)
        SharedPreferences sharedPreferences = getSharedPreferences("MedilinkPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

        if (userId == 0) {
            // User ID not found, handle accordingly
            return;
        }

        // Get a reference to the NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        // Get references to the TextViews in the header
        TextView navUsername = headerView.findViewById(R.id.nav_user_name);
        TextView navUserRole = headerView.findViewById(R.id.nav_user_role);

        // Create the URL with the user ID parameter
        String url = USER_PROFILE_URL + "?user_id=" + userId;

        // Make the API request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONObject userObj = jsonResponse.getJSONObject("user");
                            String fullName = userObj.getString("full_name");
                            String role = userObj.getString("role");

                            // Update the TextViews with the user data
                            navUsername.setText(fullName);
                            navUserRole.setText(role);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("HomeActivity", "Error parsing user profile JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("HomeActivity", "Error fetching user profile: " + error.toString())
        );
        queue.add(stringRequest);
    }
    private void fetchProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCTS_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray productsArray = jsonResponse.getJSONArray("products");
                            productList.clear();

                            for (int i = 0; i < productsArray.length(); i++) {
                                JSONObject productObj = productsArray.getJSONObject(i);
                                Product product = new Product(
                                        productObj.getInt("product_id"),
                                        productObj.getString("name"),
                                        productObj.getString("description"),
                                        productObj.getInt("stock"),
                                        productObj.getDouble("price"),
                                        productObj.getString("unit")
                                );
                                productList.add(product);
                            }
                            productAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("HomeActivity", "Error: " + error.toString())
        );
        queue.add(stringRequest);
    }
    private void showModal() {
        // Create Dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
