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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static com.android.telm.MainActivity.ip;

public class DoctorsMainMenuActivity extends AppCompatActivity {
    private Button findPatientButton, addPatientButton, logoutButton, accountButton;
    private TextView professionTextView, doctorNameTextView, studiesNumberTextView, patientsNumberTextView;
    String token, doctorName, doctorSurname;

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
        patientsNumberTextView = findViewById(R.id.patientsNumberTextView);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        getLoginData();
        getCountStudiesForDoctor();
        getCountPatientsForDoctor();

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
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutCurrentDoctor();
            }
        });
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorDetailsActivity();
            }
        });
    }

    private void goToDoctorDetailsActivity() {
        Intent intent = new Intent(this, AccountDoctorDetailsActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Aby wyjść z aplikacji naciśnij przycisk Wyloguj", Toast.LENGTH_SHORT).show();
        return;
    }
    private void logoutCurrentDoctor() {

        Intent intent = new Intent(this, MainActivity.class);
        token = "";
        intent.putExtra("token", token);
        startActivity(intent);
        Toast.makeText(this, "Zostałeś wylogowany.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getLoginData() {

        String URL = "http://"+ip+":8080/me";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    doctorName = response.getString("name");
                    doctorSurname = response.getString("surname");
                    doctorNameTextView.setText(doctorName + " " + doctorSurname);

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

    private void getCountStudiesForDoctor() {
        String URL = "http://"+ip+":8080/api/doctor/countstudies";

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                studiesNumberTextView.setText(response);
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

    private void getCountPatientsForDoctor() {
        String URL = "http://"+ip+":8080/api/doctor/patient/count";

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        patientsNumberTextView.setText(response);
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

    private void goToPatientsList() {
        Intent intent = new Intent(getApplicationContext(), SearchForPatientActivity.class);
        intent.putExtra("token", token);
        System.out.println(token);
        startActivity(intent);
    }

    private void goToPatientCreation() {

        Intent intent = new Intent(this, PatientCreationActivity.class);
        intent.putExtra("token", token);
        System.out.println(token);
        startActivity(intent);
    }
}
