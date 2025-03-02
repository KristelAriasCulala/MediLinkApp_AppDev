package com.example.medilink_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.model.GradientColor;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class ActivityProfile extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView profileImage;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        profileImage = findViewById(R.id.profile_image);
        barChart = findViewById(R.id.barChart);

        // Open drawer when profile image is clicked
        profileImage.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));

        // Handle Navigation Drawer Item Clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ActivityProfile.this, HomeActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ActivityProfile.this, ActivityProfile.class));
            } else if (id == R.id.nav_product) {
                startActivity(new Intent(ActivityProfile.this, ActivityProduct.class));
            }
            drawerLayout.closeDrawer(Gravity.START); // Close drawer after click
            return true;
        });

        // Initialize Bar Chart
        setupBarChart();
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 160.5f)); // Monday
        entries.add(new BarEntry(1f, 170.3f)); // Tuesday
        entries.add(new BarEntry(2f, 155.2f)); // Wednesday
        entries.add(new BarEntry(3f, 180.1f)); // Thursday
        entries.add(new BarEntry(4f, 175.6f)); // Friday
        entries.add(new BarEntry(5f, 190.2f)); // Saturday
        entries.add(new BarEntry(6f, 200.0f)); // Sunday

        BarDataSet dataSet = new BarDataSet(entries, "Calories Burned");
        dataSet.setValueTextSize(12f);

        // Apply Gradient Colors
        List<GradientColor> gradientColors = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            gradientColors.add(new GradientColor(0xFF99DFEC, 0xFF0255A3)); // Light blue to dark blue
        }
        dataSet.setGradientColors(gradientColors);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate(); // Refresh the chart

        // Customize X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        // Customize Y-Axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
    }
}
