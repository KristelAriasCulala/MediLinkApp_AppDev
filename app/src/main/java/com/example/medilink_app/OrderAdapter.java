package com.example.medilink_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Order ID: #" + order.getOrderId());
        holder.tvOrderDate.setText("Date: " + order.getOrderDate());
        holder.tvOrderStatus.setText("Status: " + order.getOrderStatus()); // Fixed method
        holder.tvTotalAmount.setText("Total: " + order.getTotalAmount());

        // Handle button clicks
        holder.btnViewDetails.setOnClickListener(v -> {
            // Handle "View Details" action
        });

        holder.btnCancelOrder.setOnClickListener(v -> {
            // Handle "Cancel Order" action
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvTotalAmount;
        Button btnViewDetails, btnCancelOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
            btnViewDetails = itemView.findViewById(R.id.btn_view_details);
            btnCancelOrder = itemView.findViewById(R.id.btn_cancel_order);
        }
    }
}
