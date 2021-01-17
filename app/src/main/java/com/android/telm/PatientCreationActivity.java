package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PatientCreationActivity extends AppCompatActivity {

    private Button addPatientButton, goToPreviousActivityButton;
    private EditText nameEditText, surnameEditText, peselEditText;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_creation);
        addPatientButton = findViewById(R.id.showMyStudiesButton);
        goToPreviousActivityButton = findViewById(R.id.goToPreviousActivityButton);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        peselEditText = findViewById(R.id.peselEditText);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postDataOfNewPatients();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void postDataOfNewPatients() throws JSONException {

        String URL = "http://192.168.99.1:8080/api/doctor/patient/add";
        final JSONObject jsonBody = new JSONObject();
        final String name = nameEditText.getText().toString();
        final String surname = surnameEditText.getText().toString();
        final String pesel = peselEditText.getText().toString();

        jsonBody.put("name", name);
        jsonBody.put("surname", surname);
        jsonBody.put("pesel", pesel);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY", response.toString());
                        Intent intent = new Intent(getApplicationContext(),
                                PatientDataFromDocPointOfViewActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("name", name);
                        intent.putExtra("surname", surname);
                        intent.putExtra("pesel", pesel);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Założono nową kartotekę dla pacjenta "
                                + name + " " + surname, Toast.LENGTH_SHORT).show();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Problem", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext()
                                ,"Błąd w zakładaniu kartoteki"
                                ,Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("surname", surname);
                params.put("pesel", pesel);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;

            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(request);
    }
}