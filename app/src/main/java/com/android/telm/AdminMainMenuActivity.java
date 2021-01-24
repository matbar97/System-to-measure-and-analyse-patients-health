package com.android.telm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.android.telm.MainActivity.ip;

public class AdminMainMenuActivity extends AppCompatActivity {

    Button showPatientsBtn, showDoctorsBtn, logoutBtn;
    String token;
    TextView doctorNumberAdminTextView, patientNumberAdminTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_menu);
        logoutBtn = findViewById(R.id.logoutButtonAdmin);
        showDoctorsBtn = findViewById(R.id.showDoctorsBtnAdmin);
        showPatientsBtn = findViewById(R.id.showPatientsBtnAdmin);
        doctorNumberAdminTextView = findViewById(R.id.doctorsNumberTextViewAdmin);
        patientNumberAdminTextView = findViewById(R.id.patientsNumberTextViewAdmin);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "Zostałeś wylogowany.", Toast.LENGTH_SHORT).show();
            }
        });

        showDoctorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShowDoctorsAdmin();
            }
        });

        showPatientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShowPatientsAdmin();
            }
        });

        token = getIntent().getStringExtra("token");
        getCountPatientsForAdmin();
        getCountDoctorsForAdmin();
    }

    private void getShowPatientsAdmin() {
        Intent intent = new Intent(getApplicationContext(), ListOfPatientsActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    private void getShowDoctorsAdmin() {
        Intent intent = new Intent(getApplicationContext(), ListOfDoctorsActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Aby wyjść z aplikacji naciśnij przycisk Wyloguj", Toast.LENGTH_SHORT).show();
        return;
    }

    private void getCountDoctorsForAdmin() {
        String URL = "http://"+ip+":8080/api/admin/doctor/count";

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        doctorNumberAdminTextView.setText(response);
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

    private void getCountPatientsForAdmin() {
        String URL = "http://"+ip+":8080/api/admin/patient/count";

        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        patientNumberAdminTextView.setText(response);
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

}
