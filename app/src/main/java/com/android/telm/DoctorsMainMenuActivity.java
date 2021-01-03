package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoctorsMainMenuActivity extends AppCompatActivity {
    private Button findPatientButton, addPatientButton, logoutButton, accountButton;
    private TextView professionTextView, doctorNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_main_menu);
        findPatientButton = findViewById(R.id.findPatientButton);
        addPatientButton = findViewById(R.id.addPatientButton);
        logoutButton = findViewById(R.id.logoutButton);
        accountButton = findViewById(R.id.accountButton);
        professionTextView = findViewById(R.id.professionTextView);
        doctorNameTextView = findViewById(R.id.doctorNameTextView);
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientCreation();
            }
        });
    }

    private void goToPatientCreation() {
        Intent intent = new Intent(this, PatientCreationActivity.class);
        startActivity(intent);
    }
}
