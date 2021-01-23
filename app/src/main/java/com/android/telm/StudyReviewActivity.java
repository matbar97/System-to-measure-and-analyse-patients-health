package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StudyReviewActivity extends AppCompatActivity {

    private Button backReviewStudyButton;
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

        Intent intent = getIntent();
        token = intent.getStringExtra("token"); doctorWholeName = intent.getStringExtra("doctorName");
        observations = intent.getStringExtra("observations");
        dateOfStudy = intent.getStringExtra("date");
        dateOfRealStudy = intent.getStringExtra("dateOfStudy");
        namePatient = intent.getStringExtra("name");
        surnamePatient = intent.getStringExtra("surname");
        peselPatient = intent.getStringExtra("peselPatient");

        observationsReviewStudyTextView.setText(observations.replace("Obserwacje: ", ""));
        doctorNameReviewStudyTextView.setText(doctorWholeName.replace("Badał: ", ""));
        studyDateReviewStudyTextView.setText(dateOfRealStudy.replace("Data badania: ", ""));
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
