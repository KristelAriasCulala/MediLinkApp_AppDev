package com.example.medilink_app;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvStatus, tvSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        tvStatus = findViewById(R.id.tv_status);
        tvSchedule = findViewById(R.id.tv_schedule);

        // Set listener for CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(selectedDate.getTime());
            updatePharmacyStatus(dayOfWeek);
        });
        // Default status for today
        Calendar today = Calendar.getInstance();
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(today.getTime());
        updatePharmacyStatus(dayOfWeek);
    }

    private void updatePharmacyStatus(String dayOfWeek) {
        String status, schedule;

        switch (dayOfWeek.toLowerCase()) {
            case "monday":
            case "tuesday":
            case "wednesday":
            case "thursday":
            case "friday":
                status = "Pharmacy is OPEN";
                schedule = "Opening Hours: 8:00 AM - 10:00 PM";
                break;
            case "saturday":
                status = "Pharmacy is OPEN";
                schedule = "Opening Hours: 9:00 AM - 6:00 PM";
                break;
            case "sunday":
                status = "Pharmacy is CLOSED";
                schedule = "Closed on Sundays";
                break;
            default:
                status = "Invalid Day";
                schedule = "No schedule available";
                break;
        }

        // Update TextViews
        tvStatus.setText(status);
        tvSchedule.setText(schedule);
    }
}