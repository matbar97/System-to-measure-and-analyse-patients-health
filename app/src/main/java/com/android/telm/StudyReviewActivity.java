package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class StudyReviewActivity extends AppCompatActivity {

    private Button backReviewStudyButton;
    private TextView patientNameReviewStudyTextView, studyDateReviewStudyTextView,
            doctorNameReviewStudyTextView, observationsReviewStudyTextView;
    String token, name, surname, pesel, observations;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_review);
        backReviewStudyButton = findViewById(R.id.backReviewStudyButton);
        patientNameReviewStudyTextView = findViewById(R.id.patientNameReviewStudyTextView);
        studyDateReviewStudyTextView = findViewById(R.id.studyDateReviewStudyTextView);
        doctorNameReviewStudyTextView = findViewById(R.id.doctorNameReviewStudyTextView);
        observationsReviewStudyTextView = findViewById(R.id.observationsReviewStudyTextView);

//        getStudyCurrentStudyData();
    }

//    private void getStudyCurrentStudyData() {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        String URL = "http://192.168.99.1:8080/api/doctor/patient/" + pesel + "/liststudies";
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
//                null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Response", response.toString());
//
////                    id = response.getInt("id");
////                    String docName = response.getString("name");
////                    String docSurname = response.getString("surname");
//
////                    patientNameTextView.setText(response.getString("name")
////                            + " " + response.getString("surname"));
//                Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent intent = getIntent();
//                String token = intent.getStringExtra("token");
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.start();
//        queue.add(req);
//
//    }
}
