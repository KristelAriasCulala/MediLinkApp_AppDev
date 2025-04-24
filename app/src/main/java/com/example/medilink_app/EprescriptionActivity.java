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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EprescriptionActivity extends AppCompatActivity {

    private EditText etPatientName, etPatientAge, etDoctorName, etMedicines;
    private Button btnSave;
    private ImageButton btnBack;

    private static final String SAVE_PRESCRIPTION_URL = "http://192.168.0.18/MedilinkX_Web/Config/app_api/save_prescription.php"; // Replace with your actual URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_prescription_activity);

        // Initialize views
        etPatientName = findViewById(R.id.et_patient_name);
        etPatientAge = findViewById(R.id.et_patient_age);
        etDoctorName = findViewById(R.id.et_doctor_name);
        etMedicines = findViewById(R.id.et_medicines);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back); // Initialize btnBack

        // Back Navigation Button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and go back
            }
        });

        btnSave.getBackground().setTintList(null); // Remove any applied tint
        btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        btnSave.setBackgroundResource(R.drawable.rounded_button_black);
        // Save Prescription Button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = etPatientName.getText().toString();
                String patientAge = etPatientAge.getText().toString();
                String doctorName = etDoctorName.getText().toString();
                String medicines = etMedicines.getText().toString();

                if (patientName.isEmpty() || patientAge.isEmpty() || doctorName.isEmpty() || medicines.isEmpty()) {
                    Toast.makeText(EprescriptionActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    savePrescription(doctorName, patientName, medicines);
                }
            }
        });
    }

    private void savePrescription(String doctorName, String patientName, String medicines) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SAVE_PRESCRIPTION_URL,
                response -> {
                    Toast.makeText(EprescriptionActivity.this, "Prescription Saved!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(EprescriptionActivity.this, "Error saving prescription: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("doctor_name", doctorName);
                params.put("patient_name", patientName);
                params.put("medicines", medicines);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}