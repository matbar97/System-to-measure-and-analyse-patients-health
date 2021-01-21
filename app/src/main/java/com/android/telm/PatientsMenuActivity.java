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

public class PatientsMenuActivity extends AppCompatActivity {

    private TextView patientNameTextView, patientStudiesNumberTextView;
    private Button myStudiesPatientButton, logoutButtonPatient, accountButtonPatient;
    String myNamePatient, mySurnamePatient, token, myPeselPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_menu);

        patientNameTextView = findViewById(R.id.patientNameTextView);
        patientStudiesNumberTextView = findViewById(R.id.patientStudiesNumberTextView);
        myStudiesPatientButton = findViewById(R.id.myStudiesButton);
        logoutButtonPatient = findViewById(R.id.logoutPatientButton);
        accountButtonPatient = findViewById(R.id.accountPatientButton);

        getPatientData();
        getCountStudiesForPatient();

        myStudiesPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyStudiesActivityPatient();
            }
        });
        logoutButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutCurrentPatient();
            }
        });


        accountButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientsDetailsActivity();
            }
        });
    }

    private void goToPatientsDetailsActivity() {
        Intent intent = new Intent(this, AccountDetailsPatientActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Aby wyjść z aplikacji naciśnij przycisk Wyloguj", Toast.LENGTH_SHORT).show();
        return;
    }

    private void logoutCurrentPatient() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Zostałeś wylogowany.", Toast.LENGTH_SHORT).show();
        token = null;
        finish();

    }

    private void goToMyStudiesActivityPatient() {
        Intent intent = new Intent(getApplicationContext(), PatientCardDataFromPatientPointOfView.class);
        intent.putExtra("myNamePatient", myNamePatient);
        intent.putExtra("mySurnamePatient", mySurnamePatient);
        intent.putExtra("token", token);
        intent.putExtra("myPeselPatient", myPeselPatient);
        startActivity(intent);
    }

    private void getCountStudiesForPatient() {
        String URL = "http://"+ip+":8080/api/patient/countstudies";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                patientStudiesNumberTextView.setText(response);
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


    private void getPatientData() {

        String URL = "http://"+ip+":8080/me";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    myNamePatient = response.getString("name");
                    mySurnamePatient = response.getString("surname");
                    myPeselPatient = response.getString("pesel");
                    patientNameTextView.setText(myNamePatient +" "+mySurnamePatient);

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
