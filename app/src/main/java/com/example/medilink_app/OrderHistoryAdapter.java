package com.example.medilink_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private OnArchiveClickListener archiveClickListener;

    public interface OnArchiveClickListener {
        void onArchiveClick(int orderId);
    }

    public OrderHistoryAdapter(Context context, List<Order> orders, OnArchiveClickListener archiveClickListener) {
        this.context = context;
        this.orders = orders;
        this.archiveClickListener = archiveClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderDate.setText(order.getOrderDate());
        holder.totalAmount.setText(String.format("₱%.2f", order.getTotalAmount()));
        holder.archiveButton.setOnClickListener(v -> archiveClickListener.onArchiveClick(order.getOrderId()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, totalAmount;
        Button archiveButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            totalAmount = itemView.findViewById(R.id.total_amount);
            archiveButton = itemView.findViewById(R.id.archive_button);
        }
    }
}