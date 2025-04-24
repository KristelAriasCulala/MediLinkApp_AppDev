package com.example.medilink_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/login.php";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        RelativeLayout loginButton = findViewById(R.id.loginButton);
        TextView signUpLink = findViewById(R.id.signUpLink);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call login API
            loginUser(username, password);
        });

        signUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String username, String password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    Log.d(TAG, "Raw login response: " + response);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            // Extract user info
                            JSONObject userObj = jsonResponse.getJSONObject("user");
                            String role = userObj.getString("role");
                            int userId = userObj.getInt("user_id");

                            // Save user ID to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MedilinkPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("user_id", userId);
                            editor.apply();

                            // Show success message and navigate to dashboard
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("role", role);  // Pass role to next screen if needed
                            startActivity(intent);
                            finish();  // Close the login screen
                        } else {
                            // Show error message from server
                            String message = jsonResponse.getString("message");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON parsing error: " + e.getMessage() + " on data: " + response);
                        Toast.makeText(LoginActivity.this, "Response format error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Login error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    Toast.makeText(LoginActivity.this, "Network error: Check your connection", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Add timeout to the request
        stringRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                15000,  // 15 seconds timeout
                1,      // Max retries
                1f      // Backoff multiplier
        ));

        queue.add(stringRequest);
    }
}