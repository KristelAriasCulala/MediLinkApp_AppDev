package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class HealthAdvicesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_advices_activity);

        // Share Tip 1
        Button btnShareTip1 = findViewById(R.id.btn_share_tip_1);
        btnShareTip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTip("Stay Hydrated", "Drink at least 8 glasses of water daily to maintain good health.");
            }
        });

        // Share Tip 2
        Button btnShareTip2 = findViewById(R.id.btn_share_tip_2);
        btnShareTip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTip("Exercise Regularly", "Engage in at least 30 minutes of physical activity daily.");
            }
        });

        // Share Tip 3
        Button btnShareTip3 = findViewById(R.id.btn_share_tip_3);
        btnShareTip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTip("Limit Unhealthy Foods and Eat Healthy Meals", "Do not forget to eat breakfast and choose a nutritious meal with more protein and fiber and less fat, sugar, and\n" +
                        "calories");
            }
        });
    }

    private void shareTip(String title, String message) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(shareIntent, "Share Tip"));
    }

}
