package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddStudyActivity extends AppCompatActivity {

    private Button backAddStudyButton, addAnnotationAddStudyButton, applyStudyAddStudyButton;
    private EditText studyDateAddStudyEditText, doctorNameAddStudyEditText, observationsMultiLineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study);
        backAddStudyButton = findViewById(R.id.backAddStudyButton);
        addAnnotationAddStudyButton = findViewById(R.id.addAnnotationAddStudyButton);
        applyStudyAddStudyButton = findViewById(R.id.applyStudyAddStudyButton);
        studyDateAddStudyEditText = findViewById(R.id.studyDateAddStudyEditText);
        doctorNameAddStudyEditText = findViewById(R.id.doctorNameAddStudyEditText);
        observationsMultiLineTextView = findViewById(R.id.observationsAddStudyEditText);

        applyStudyAddStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudyReview();
            }
        });
    }

    private void goToStudyReview() {
        Intent intent = new Intent(this, StudyReviewActivity.class);
        startActivity(intent);
    }
}
