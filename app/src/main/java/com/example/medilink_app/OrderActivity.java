package com.example.medilink_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity  extends AppCompatActivity  {
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recycler_view_orders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Initialize order list
        orderList = new ArrayList<>();
        orderList.add(new Order("12345", "2023-10-01", "Delivered", "$50.00"));
        orderList.add(new Order("67890", "2023-09-28", "Pending", "$30.00"));
        orderList.add(new Order("54321", "2023-09-25", "Cancelled", "$20.00"));

        // Set up adapter
        orderAdapter = new OrderAdapter(orderList);
        recyclerViewOrders.setAdapter(orderAdapter);
    }
}
