package com.example.medilink_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityOrderHistory extends AppCompatActivity {
    private static final String ORDER_HISTORY_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/get_order_history.php";
    private static final String ARCHIVE_ORDER_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/archive_order.php";

    private RecyclerView orderRecyclerView;
    private OrderHistoryAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Initialize RecyclerView and Adapter
        orderRecyclerView = findViewById(R.id.order_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrderHistoryAdapter(this, orderList, this::archiveOrder);
        orderRecyclerView.setAdapter(orderAdapter);

        // Fetch completed orders
        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        SharedPreferences prefs = getSharedPreferences("MedilinkPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", 0);
        if (userId == 0) {
            Toast.makeText(this, "Please log in to view your order history", Toast.LENGTH_LONG).show();
            return;
        }

        String url = ORDER_HISTORY_URL + "?user_id=" + userId;
        Log.d("OrderHistory", "Request URL: " + url); // Log the complete URL

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.d("OrderHistory", "API Response: " + response);
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray ordersArray = jsonResponse.getJSONArray("orders");
                            orderList.clear();

                            for (int i = 0; i < ordersArray.length(); i++) {
                                JSONObject orderObj = ordersArray.getJSONObject(i);
                                // Extract data directly from orderObj
                                int orderId = orderObj.getInt("order_id");
                                String orderDate = orderObj.getString("order_date");
                                double totalAmount = orderObj.getDouble("total_amount");

                                // Create Order object and add to the list
                                Order order = new Order(orderId, orderDate, totalAmount, "completed"); // Status is already 'completed'
                                orderList.add(order);
                            }

                            Log.d("OrderHistory", "Orders loaded: " + orderList.size());
                            orderAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No completed orders found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("OrderHistory", "Error parsing JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("OrderHistory", "Error fetching orders: " + error.toString())
        );

        queue.add(request);
    }

    private void archiveOrder(int orderId) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("order_id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, ARCHIVE_ORDER_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(this, "Order archived successfully.", Toast.LENGTH_SHORT).show();
                            fetchOrderHistory(); // Refresh the list
                        } else {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("OrderHistory", "Error parsing JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("OrderHistory", "Error archiving order: " + error.toString())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return postData.toString().getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        queue.add(request);
    }
    public interface OnArchiveClickListener {
        void onArchiveClick(int orderId);
    }

    private void archiveOrder(int orderId, OnArchiveClickListener listener) {
        listener.onArchiveClick(orderId);
    }
}