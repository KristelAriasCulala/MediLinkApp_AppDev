package com.example.medilink_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityCart extends AppCompatActivity {
    private static final String CART_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/get_cart_items.php";

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView txtSubtotal, txtTaxes, txtTotal;
    private Button btnCheckout;
    private int orderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        txtSubtotal = findViewById(R.id.txt_subtotal);
        txtTaxes = findViewById(R.id.txt_taxes);
        txtTotal = findViewById(R.id.txt_total);
        btnCheckout = findViewById(R.id.btn_checkout);

        // Set up RecyclerView
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Fetch cart items
        fetchCartItems();

        // Set up checkout button
        btnCheckout.setOnClickListener(v -> {
            if (orderId > 0) {
                String url = "http://192.168.0.18/MedilinkX_Web/Config/app_api/checkout.php";

                JSONObject postData = new JSONObject();
                try {
                    postData.put("order_id", orderId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(ActivityCart.this);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                        response -> {
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(ActivityCart.this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                                    cartItems.clear(); // Clear cart items
                                    cartAdapter.notifyDataSetChanged(); // Refresh adapter
                                    txtSubtotal.setText("₱0.00");
                                    txtTaxes.setText("₱0.00");
                                    txtTotal.setText("₱0.00");
                                    btnCheckout.setEnabled(false); // Disable checkout button
                                } else {
                                    Toast.makeText(ActivityCart.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ActivityCart.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Log.e("ActivityCart", "Error: " + error.toString());
                            Toast.makeText(ActivityCart.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                queue.add(request);
            } else {
                Toast.makeText(ActivityCart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void fetchCartItems() {
        // Get user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MedilinkPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

        if (userId == 0) {
            Toast.makeText(this, "Please log in to view your cart", Toast.LENGTH_LONG).show();
            return;
        }

        String url = CART_URL + "?user_id=" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            // Get order ID
                            orderId = jsonResponse.getInt("order_id");

                            // Clear existing items
                            cartItems.clear();

                            // Parse cart items
                            JSONArray itemsArray = jsonResponse.getJSONArray("cart_items");
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject item = itemsArray.getJSONObject(i);
                                cartItems.add(new CartItem(
                                        item.getInt("order_item_id"),
                                        item.getInt("product_id"),
                                        item.getString("product_name"),
                                        item.getInt("quantity"),
                                        item.getDouble("price_per_unit"),
                                        item.getDouble("subtotal")
                                ));
                            }

                            // Update adapter
                            cartAdapter.notifyDataSetChanged();

                            // Update totals
                            double subtotal = jsonResponse.getDouble("subtotal");
                            double tax = jsonResponse.getDouble("tax");
                            double total = jsonResponse.getDouble("total");

                            txtSubtotal.setText(String.format("₱%.2f", subtotal));
                            txtTaxes.setText(String.format("₱%.2f", tax));
                            txtTotal.setText(String.format("₱%.2f", total));

                            // Enable checkout button
                            btnCheckout.setEnabled(true);

                        } else if (status.equals("empty")) {
                            // Handle empty cart
                            Toast.makeText(ActivityCart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                            btnCheckout.setEnabled(false);
                        } else {
                            // Handle error
                            Toast.makeText(ActivityCart.this, "Error: " + jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e("CartActivity", "Error parsing JSON: " + e.getMessage());
                        Toast.makeText(ActivityCart.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("CartActivity", "Error fetching cart: " + error.toString());
                    Toast.makeText(ActivityCart.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }
}