package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorsMainMenuActivity extends AppCompatActivity {
    private Button findPatientButton, addPatientButton, logoutButton, accountButton;
    private TextView professionTextView, doctorNameTextView, studiesNumberTextView;

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
        studiesNumberTextView = findViewById(R.id.studiesNumberTextView);

        getLoginData();
        getCountStudiesForDoctor();

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientCreation();
            }
        });
        findPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientsList();
            }
        });
    }

    private void getLoginData() {

        String URL = "http://192.168.99.1:8080/me";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    String docName = response.getString("name");
                    String docSurname = response.getString("surname");

                    doctorNameTextView.setText(response.getString("name")
                            + " " + response.getString("surname"));
//                    doctorNameTextView.setText(response.getString("Authorization"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent intent = getIntent();
//                String name = intent.getStringExtra("username");
//                Intent intent2 = getIntent();
//                String password = intent.getStringExtra("password");


//                String credentials = name + ":" + password;
//                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                String auth = "Bearer " + base64EncodedCredentials;
//                headers.put("Authorization", auth);
                headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2N0b3IiLCJyb2xlcyI6WyJST0xFX0RPQ1RPUiJdLCJpYXQiOjE2MTA2NTg3NzAsImV4cCI6MTYxMDY2MjM3MH0.bpShJintGKonzR4H2IbfI2JeG_odreJIQtRkB0ZrLxI");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        queue.add(req);
    }

    private void getCountStudiesForDoctor() {
        String URL = "http://192.168.99.1:8080/api/doctor/countstudies";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                studiesNumberTextView.setText(response.toString());

                Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2N0b3IiLCJyb2xlcyI6WyJST0xFX0RPQ1RPUiJdLCJpYXQiOjE2MTA2NTg3NzAsImV4cCI6MTYxMDY2MjM3MH0.bpShJintGKonzR4H2IbfI2JeG_odreJIQtRkB0ZrLxI");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        queue.add(req);
    }

    private void goToPatientsList() {
        Intent intent = new Intent(this, SearchForPatientActivity.class);
        startActivity(intent);
    }

    private void goToPatientCreation() {
        Intent intent = new Intent(this, PatientCreationActivity.class);
        startActivity(intent);
    }
}
