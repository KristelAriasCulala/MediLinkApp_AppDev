package com.example.medilink_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EprescriptionActivity extends AppCompatActivity {

    private EditText etPatientName, etPatientAge, etDoctorName, etMedicines;
    private Button btnSave, btnPrint;
    private ImageButton btnBack;

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
        btnPrint = findViewById(R.id.btn_print);
        btnBack = findViewById(R.id.btn_back); // Initialize btnBack

        // Back Navigation Button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and go back
            }
        });

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
                    // Simulate saving the prescription
                    Toast.makeText(EprescriptionActivity.this, "Prescription Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Print Button
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = etPatientName.getText().toString();
                String patientAge = etPatientAge.getText().toString();
                String doctorName = etDoctorName.getText().toString();
                String medicines = etMedicines.getText().toString();

                if (patientName.isEmpty() || patientAge.isEmpty() || doctorName.isEmpty() || medicines.isEmpty()) {
                    Toast.makeText(EprescriptionActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Simulate printing the prescription
                    Toast.makeText(EprescriptionActivity.this, "Printing Prescription...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}