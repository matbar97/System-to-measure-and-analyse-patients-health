package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientDataFromDocPointOfViewActivity extends AppCompatActivity {

    private Button goBackButton_PatientData, addStudyButton;
    private TextView actualPatientNameImageView, numberOfRecordsTextView;
    String token, name, surname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data_from_doc_point_of_view);
        goBackButton_PatientData = findViewById(R.id.goBackButton);
        addStudyButton = findViewById(R.id.addStudyButton);
        actualPatientNameImageView = findViewById(R.id.actualPatientNameImageView);
        numberOfRecordsTextView = findViewById(R.id.numberOfRecordsTextView);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");

        addStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudyCreation();
            }
        });
        getPatientCardCreatedByDoctor();

    }

    private void getPatientCardCreatedByDoctor() {
        String URL = "http://192.168.99.1:8080/me";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                actualPatientNameImageView.setText(name + " " + surname);

                Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                Intent intent = getIntent();
                token = intent.getStringExtra("token");
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        queue.add(req);
    }

    private void goToStudyCreation() {
        Intent intent = new Intent(this, AddStudyActivity.class);
        startActivity(intent);
    }
}
