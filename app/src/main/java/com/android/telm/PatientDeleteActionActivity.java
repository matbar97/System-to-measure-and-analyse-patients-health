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

public class PatientDeleteActionActivity extends AppCompatActivity {

    Button deletePatientBtn, arrowBack;
    TextView userPatientLogin;
    String token, username;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_delete_action);

        deletePatientBtn = findViewById(R.id.deletePatientButton);
        userPatientLogin = findViewById(R.id.loginPatientAdminDeleteTxtView);
        arrowBack = findViewById(R.id.backToPatientsListBtn);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getStringExtra("id");
        username = intent.getStringExtra("username");

        userPatientLogin.setText(username);

        deletePatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserPatient();
            }
        });



    }

    private void deleteUserPatient() {
        String URL = "http://"+ip+":8080/api/admin/patient/" + id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                Toast.makeText(getApplicationContext(), "Użytkownik (pacjent) "
                        + username + " został usunięty.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ListOfPatientsActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);

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
