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

import static com.android.telm.MainActivity.ip;

public class AccountDetailsPatientActivity extends AppCompatActivity {

    private Button goBackToPatientMainMenuButton;
    private TextView patientDetailsNameTextView, patientDetailsPeselTextView;
    String patientName, patientSurname, patientPesel, token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details_patient);

        goBackToPatientMainMenuButton = findViewById(R.id.goBackButtonPatientDetailsToMainMenu);
        patientDetailsNameTextView = findViewById(R.id.actualPatientDetailsNameTextView);
        patientDetailsPeselTextView = findViewById(R.id.peselPatientDetailsTextView);

        goBackToPatientMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getPatientDetails();
    }

    private void getPatientDetails() {
        String URL = "http://"+ip+":8080/me";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    patientName = response.getString("name");
                    patientSurname = response.getString("surname");
                    patientPesel = response.getString("pesel");
                    patientDetailsNameTextView.setText(patientName + " " + patientSurname);
                    patientDetailsPeselTextView.setText(patientPesel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
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
