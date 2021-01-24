package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StudyReviewActivity extends AppCompatActivity {

    private Button backReviewStudyButton, goToStudyEditionButton;
    private TextView patientNameReviewStudyTextView, studyDateReviewStudyTextView, addedStudyDateReviewStudyTextView,
            doctorNameReviewStudyTextView, observationsReviewStudyTextView, dateOfStudyTextView;
    String token, doctorWholeName, namePatient, surnamePatient, peselPatient, observations, dateOfStudy, dateOfRealStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_review);
        backReviewStudyButton = findViewById(R.id.backReviewStudyButton);
        patientNameReviewStudyTextView = findViewById(R.id.patientNameReviewStudyTextView);
        studyDateReviewStudyTextView = findViewById(R.id.studyDateReviewStudyTextView);
        doctorNameReviewStudyTextView = findViewById(R.id.doctorNameReviewStudyTextView);
        observationsReviewStudyTextView = findViewById(R.id.observationsReviewStudyTextView);
        addedStudyDateReviewStudyTextView = findViewById(R.id.dateOfStudyTextView2);
        goToStudyEditionButton = findViewById(R.id.goToStudyEdtionButton);

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

        goToStudyEditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudyEdition();
            }
        });
    }

    private void goToStudyEdition() {
        Intent intent = new Intent(getApplicationContext(), EditStudyActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("doctorName", doctorWholeName);
        intent.putExtra("observations", observations);
        intent.putExtra("date", dateOfStudy);
        intent.putExtra("dateOfStudy", dateOfRealStudy);
        intent.putExtra("name", namePatient);
        intent.putExtra("surname", surnamePatient);
        intent.putExtra("peselPatient", peselPatient);
        startActivity(intent);
    }

    private void goBackToPatientsCardPatientAccount() {
        onBackPressed();
    }

}
