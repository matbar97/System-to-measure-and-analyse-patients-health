package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PatientDataFromDocPointOfViewActivity extends AppCompatActivity {

    private Button goBackButton_PatientData, addStudyButton;
    private TextView actualPatientNameImageView, numberOfRecordsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data_from_doc_point_of_view);
        goBackButton_PatientData = findViewById(R.id.goBackButton);
        addStudyButton = findViewById(R.id.addStudyButton);
        actualPatientNameImageView = findViewById(R.id.actualPatientNameImageView);
        numberOfRecordsTextView = findViewById(R.id.numberOfRecordsTextView);

        addStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudyCreation();
            }
        });

    }

    private void goToStudyCreation() {
        Intent intent = new Intent(this, AddStudyActivity.class);
        startActivity(intent);
    }
}
