package com.example.medilink_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Account_management extends AppCompatActivity {
    private LinearLayout userListContainer;
    private Button addUserButton, editUserButton, deleteUserButton;
    private EditText editName, editEmail, editPassword;
    private RequestQueue requestQueue;
    private String selectedUserId = null;

    // Use your computer's IP address or 10.0.2.2 for emulator
    private static final String BASE_URL = "http://192.168.8.41/crud/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        // Initialize UI components
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        userListContainer = findViewById(R.id.userListContainer);
        addUserButton = findViewById(R.id.addUserButton);
        editUserButton = findViewById(R.id.editUserButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Load initial user data
        loadUsers();

        // Handle button clicks
        addUserButton.setOnClickListener(v -> createUser());
        editUserButton.setOnClickListener(v -> updateUser());
        deleteUserButton.setOnClickListener(v -> deleteUser());

        // Floating Action Button - clears form
        findViewById(R.id.fabAddUser).setOnClickListener(v -> {
            clearForm();
            selectedUserId = null; // Reset selected user when clearing form
        });
    }

    private void clearForm() {
        editName.setText("");
        editEmail.setText("");
        editPassword.setText("");
    }

    private void createUser() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "insert.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            clearForm();
                            loadUsers();
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void loadUsers() {
        userListContainer.removeAllViews();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL + "fetch.php", null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject user = response.getJSONObject(i);
                            addUserToView(
                                    user.getString("id"),
                                    user.getString("name"),
                                    user.getString("email")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing user data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading users: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }

    private void addUserToView(String id, String name, String email) {
        View userCard = LayoutInflater.from(this).inflate(R.layout.item_user, userListContainer, false);

        TextView userId = userCard.findViewById(R.id.userId);
        TextView userName = userCard.findViewById(R.id.userName);
        TextView userEmail = userCard.findViewById(R.id.userEmail);
        Button deleteButton = userCard.findViewById(R.id.deleteUserButton);

        userId.setText("ID: " + id);
        userName.setText("Name: " + name);
        userEmail.setText("Email: " + email);

        // Set click listener to populate form for editing
        userCard.setOnClickListener(v -> {
            selectedUserId = id;
            editName.setText(name);
            editEmail.setText(email);
            editPassword.setText(""); // Clear password field for security
        });

        // Set click listener for delete button
        deleteButton.setOnClickListener(v -> {
            selectedUserId = id;
            deleteUser();
        });

        userListContainer.addView(userCard);
    }

    private void updateUser() {
        if (selectedUserId == null) {
            Toast.makeText(this, "Please select a user to update", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "update.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            clearForm();
                            selectedUserId = null;
                            loadUsers();
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
                params.put("id", selectedUserId);
                params.put("name", name);
                params.put("email", email);
                if (!password.isEmpty()) {
                    params.put("password", password);
                }
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void deleteUser() {
        if (selectedUserId == null) {
            Toast.makeText(this, "Please select a user to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, BASE_URL + "delete.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            clearForm();
                            selectedUserId = null;
                            loadUsers();
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
                params.put("id", selectedUserId);
                return params;
            }
        };

        requestQueue.add(request);
    }
}