package com.example.medilink_app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private static final String ADD_TO_CART_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/add_to_cart.php";

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("₱%.2f %s", product.getPrice(), product.getUnit()));
        holder.productStock.setText("Stock: " + product.getStock());

        // Set click listener on the item
        holder.itemView.setOnClickListener(v -> {
            showAddToCartModal(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void showAddToCartModal(Product product) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.modal_add_to_cart);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find views in dialog
        TextView productNameTv = dialog.findViewById(R.id.modal_product_name);
        TextView productDescTv = dialog.findViewById(R.id.modal_product_description);
        TextView productPriceTv = dialog.findViewById(R.id.modal_product_price);
        NumberPicker quantityPicker = dialog.findViewById(R.id.quantity_picker);
        Button addToCartBtn = dialog.findViewById(R.id.add_to_cart_button);
        Button cancelBtn = dialog.findViewById(R.id.cancel_button);

        // Set product details
        productNameTv.setText(product.getName());
        productDescTv.setText(product.getDescription().isEmpty() ? "No description available" : product.getDescription());
        productPriceTv.setText(String.format("₱%.2f %s", product.getPrice(), product.getUnit()));

        // Configure quantity picker
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(Math.min(product.getStock(), 100)); // Limit by stock

        // Set button actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        addToCartBtn.setOnClickListener(v -> {
            int selectedQuantity = quantityPicker.getValue();
            addToCart(product.getProductId(), selectedQuantity, product.getPrice(), dialog);
        });

        dialog.show();
    }

    private void addToCart(int productId, int quantity, double price, Dialog dialog) {
        // Get userId from SharedPreferences
        android.content.SharedPreferences sharedPreferences =
                context.getSharedPreferences("MedilinkPrefs", android.content.Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

        if (userId == 0) {
            Toast.makeText(context, "Please log in to add items to cart", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, ADD_TO_CART_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("product_id", String.valueOf(productId));
                params.put("quantity", String.valueOf(quantity));
                params.put("price", String.valueOf(price));
                return params;
            }
        };

        queue.add(request);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productStock;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productStock = itemView.findViewById(R.id.product_stock);
        }
    }
}