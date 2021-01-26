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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.telm.MainActivity.ip;
import static com.android.telm.MainActivity.replace;

public class StudyReviewActivity extends AppCompatActivity {

    private Button backReviewStudyButton, goToStudyEditionButton;
    private TextView patientNameReviewStudyTextView, studyDateReviewStudyTextView, addedStudyDateReviewStudyTextView,
            doctorNameReviewStudyTextView, observationsReviewStudyTextView, dateOfStudyTextView, dateModified, studyModified;
    String token, doctorWholeName, namePatient, surnamePatient, peselPatient, observations, dateOfStudy, dateOfRealStudy, id, modified, dateOfModification, modifedBy;
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
        dateModified = findViewById(R.id.dateModified);
        studyModified = findViewById(R.id.studyModified);


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        doctorWholeName = intent.getStringExtra("doctorName").replace("Badał: ", "");
        observations = intent.getStringExtra("observations").replace("Obserwacje: ", "");
        dateOfStudy = intent.getStringExtra("date");
        dateOfRealStudy = intent.getStringExtra("dateOfStudy").replace("Data badania: ", "");
        namePatient = intent.getStringExtra("name");
        surnamePatient = intent.getStringExtra("surname");
        peselPatient = intent.getStringExtra("peselPatient");
        modified = intent.getStringExtra("modified");
        id = intent.getStringExtra("id");

        if(modified.equals("false")){
            dateModified.setVisibility(TextView.INVISIBLE);
            studyModified.setVisibility(TextView.INVISIBLE);
        }else {
            getModifiedInfo();
        }

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
        intent.putExtra("id", id);

        startActivity(intent);
    }

    private void goBackToPatientsCardPatientAccount() {
        onBackPressed();
    }

    private void getModifiedInfo() {

        String URL = "http://"+ip+":8080/api/doctor/patient/study/"+id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    dateOfModification = response.getString("dateOfModification");
                    modifedBy = response.getString("modifiedBy");
                    studyModified.setText(modifedBy);


                    dateOfModification = replace(dateOfModification, 10, ' ');
                    dateOfModification = dateOfModification.substring(0,dateOfModification.indexOf("."));
                    dateModified.setText(dateOfModification);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Intent intent = getIntent();
                token = intent.getStringExtra("token");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        queue.add(req);
    }

}
