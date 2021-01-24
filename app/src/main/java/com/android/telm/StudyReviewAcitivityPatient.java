package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StudyReviewAcitivityPatient extends AppCompatActivity {

    private Button backReviewStudyButton, goToStudyEditionButton;
    private TextView patientNameReviewStudyTextView, studyDateReviewStudyTextView, addedStudyDateReviewStudyTextView,
            doctorNameReviewStudyTextView, observationsReviewStudyTextView, dateOfStudyTextView;
    String token, doctorWholeName, namePatient, surnamePatient, peselPatient, observations, dateOfStudy, dateOfRealStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_review_acitivity_patient);
        backReviewStudyButton = findViewById(R.id.backReviewStudyButton);
        patientNameReviewStudyTextView = findViewById(R.id.patientNameReviewStudyTextView);
        studyDateReviewStudyTextView = findViewById(R.id.studyDateReviewStudyTextView);
        doctorNameReviewStudyTextView = findViewById(R.id.doctorNameReviewStudyTextView);
        observationsReviewStudyTextView = findViewById(R.id.observationsReviewStudyTextView);
        addedStudyDateReviewStudyTextView = findViewById(R.id.dateOfStudyTextView2);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        doctorWholeName = intent.getStringExtra("doctorName").replace("Badał: ", "");
        observations = intent.getStringExtra("observations").replace("Obserwacje: ", "");
        dateOfStudy = intent.getStringExtra("date");
        dateOfRealStudy = intent.getStringExtra("dateOfStudy").replace("Data badania: ", "");
        namePatient = intent.getStringExtra("name");
        surnamePatient = intent.getStringExtra("surname");
        peselPatient = intent.getStringExtra("peselPatient");

        observationsReviewStudyTextView.setText(observations);
        doctorNameReviewStudyTextView.setText(doctorWholeName);
        studyDateReviewStudyTextView.setText(dateOfRealStudy);
        patientNameReviewStudyTextView.setText(namePatient + " " + surnamePatient);
        addedStudyDateReviewStudyTextView.setText(dateOfStudy);

        backReviewStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToPatientsCardPatientAccount();
            }
        });

    }


    private void goBackToPatientsCardPatientAccount() {
        onBackPressed();
    }
}