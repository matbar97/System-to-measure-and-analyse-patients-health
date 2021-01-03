package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PatientCreationActivity extends AppCompatActivity {

    private Button addPatientButton, goToPreviousActivityButton;
    private EditText nameEditText, birthEditText, peselEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_creation);
        addPatientButton = findViewById(R.id.addPatientButton);
        goToPreviousActivityButton = findViewById(R.id.goToPreviousActivityButton);
        nameEditText = findViewById(R.id.nameEditText);
        birthEditText = findViewById(R.id.birthEditText);
        peselEditText = findViewById(R.id.peselEditText);
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientsData();
            }
        });

    }

    private void goToPatientsData() {
        Intent intent = new Intent(this, PatientDataFromDocPointOfViewActivity.class);
        startActivity(intent);
    }
}
