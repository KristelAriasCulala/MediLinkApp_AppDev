package com.example.medilink_app;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Account_management extends AppCompatActivity{
    private LinearLayout userListContainer;
    private Button addUserButton, editUserButton, deleteUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        // Initialize UI components
        userListContainer = findViewById(R.id.userListContainer);
        addUserButton = findViewById(R.id.addUserButton);
        editUserButton = findViewById(R.id.editUserButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        // Load initial user data
        loadUsers();

        // Handle button clicks
        addUserButton.setOnClickListener(v -> showToast("Add User Clicked"));
        editUserButton.setOnClickListener(v -> showToast("Edit User Clicked"));
        deleteUserButton.setOnClickListener(v -> showToast("Delete User Clicked"));

        // Floating Action Button
        findViewById(R.id.fabAddUser).setOnClickListener(v -> addUserCard());
    }

    // Load initial user data
    private void loadUsers() {
        String[] users = {"John Doe (Admin)", "Jane Smith (Pharmacist)"};
        for (String user : users) {
            addUserCard(user);
        }
    }

    // Add a new user card
    private void addUserCard(String userName) {
        View userCard = LayoutInflater.from(this).inflate(R.layout.item_user, userListContainer, false);

        TextView userNameTextView = userCard.findViewById(R.id.userName);
        userNameTextView.setText(userName);

        // Add the card to the container
        userListContainer.addView(userCard);
    }

    // Add a new user card with default text
    private void addUserCard() {
        String newUser = "New User (Role)";
        addUserCard(newUser);
    }

    // Helper method to show toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
