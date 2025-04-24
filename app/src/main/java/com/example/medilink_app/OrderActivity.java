package com.example.medilink_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private static final String ORDERS_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/get_orders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recycler_view_orders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Initialize order list
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        recyclerViewOrders.setAdapter(orderAdapter);

        // Fetch orders from the database
        fetchOrdersFromDatabase();
    }

    private void fetchOrdersFromDatabase() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ORDERS_URL, null,
                response -> {
                    try {
                        orderList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject orderObj = response.getJSONObject(i);
                            int orderId = orderObj.getInt("order_id");
                            String orderDate = orderObj.getString("order_date");
                            String orderStatus = orderObj.getString("order_status"); // Fetch status
                            double totalAmount = orderObj.getDouble("total_amount");

                            // Pass all required arguments to the Order constructor
                            orderList.add(new Order(orderId, orderDate, totalAmount, orderStatus));
                        }
                        orderAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("OrderActivity", "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(this, "Error parsing order data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("OrderActivity", "Volley error: " + error.toString());
                    Toast.makeText(this, "Failed to fetch orders.", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonArrayRequest);
    }
}