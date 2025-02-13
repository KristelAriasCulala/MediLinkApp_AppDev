package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    RelativeLayout loginButton;
    CheckBox rememberMe;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Initialize UI elements
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPassword = findViewById(R.id.forgotPassword);
// Sign Up Link Click
        TextView signUpLink = findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        // Login Button Click
        TextView loginButtonText = findViewById(R.id.loginButtonText);
        loginButtonText.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
                // Navigate to Home Screen (Replace with actual home activity)
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        // Forgot Password Click
        forgotPassword.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show());
    }
}