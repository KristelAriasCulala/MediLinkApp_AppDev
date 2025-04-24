package com.example.medilink_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static final String DELETE_CART_ITEM_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/delete_cart_item.php";
    private Context context;
    private List<CartItem> cartItems;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_row, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.productName.setText(cartItem.getProductName());
        holder.price.setText(String.format("₱%.2f", cartItem.getPricePerUnit()));
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.subtotal.setText(String.format("₱%.2f", cartItem.getSubtotal()));

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            deleteCartItem(cartItem.getOrderItemId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void deleteCartItem(int orderItemId, int position) {
        String url = DELETE_CART_ITEM_URL + "?order_item_id=" + orderItemId;

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            // Remove item from the list and notify adapter
                            cartItems.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                            // Refresh the cart
                            if (context instanceof ActivityCart) {
                                ((ActivityCart) context).fetchCartItems();
                            }
                        } else {
                            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("CartAdapter", "Error deleting item: " + error.toString());
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, price, quantity, subtotal;
        ImageView deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.item_product_name);
            price = itemView.findViewById(R.id.item_price);
            quantity = itemView.findViewById(R.id.item_quantity);
            subtotal = itemView.findViewById(R.id.item_subtotal);
            deleteButton = itemView.findViewById(R.id.item_delete);
        }
    }
}