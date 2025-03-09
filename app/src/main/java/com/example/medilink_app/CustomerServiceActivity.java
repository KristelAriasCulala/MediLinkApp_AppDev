package com.example.medilink_app;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerServiceActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMessage;
    private Button btnSubmit;
    private ImageButton btnCall, btnEmail, btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_service_activity);

        // Initialize views
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etMessage = findViewById(R.id.et_message);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCall = findViewById(R.id.btn_call);
        btnEmail = findViewById(R.id.btn_email);
        btnChat = findViewById(R.id.btn_chat);

        // Handle Submit Button
        btnSubmit.getBackground().setTintList(null); // Remove any applied tint
        btnSubmit.setBackgroundTintList(ColorStateList.valueOf(R.drawable.button_background));

        btnSubmit.setBackgroundResource(R.drawable.button_background);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String message = etMessage.getText().toString();

                if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                    Toast.makeText(CustomerServiceActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerServiceActivity.this, "Feedback Submitted!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Call Button
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerServiceActivity.this, "Calling Customer Service...", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Email Button
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerServiceActivity.this, "Opening Email...", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Chat Button
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerServiceActivity.this, "Starting Chat...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}