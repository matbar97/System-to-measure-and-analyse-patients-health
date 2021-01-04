package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SearchForPatientActivity extends AppCompatActivity {

    private Button arrowBackSearchForPatientButton;
    private EditText searchForPatientEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_patient);
        arrowBackSearchForPatientButton = findViewById(R.id.arrowBackSearchForPatientButton);
        searchForPatientEditText = findViewById(R.id.searchForPatientEditText);
    }
}
