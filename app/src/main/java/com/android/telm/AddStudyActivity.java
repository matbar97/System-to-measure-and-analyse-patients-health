package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AddStudyActivity extends AppCompatActivity {

    private Button backAddStudyButton, addAnnotationAddStudyButton, applyStudyAddStudyButton;
    private EditText studyDateAddStudyEditText, doctorNameAddStudyEditText, observationsMultiLineTextView;
    private TextView patientNameAddStudyTextView;
    String name, surname, pesel, token, observations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study);
        backAddStudyButton = findViewById(R.id.backAddStudyButton);
        addAnnotationAddStudyButton = findViewById(R.id.addAnnotationAddStudyButton);
        applyStudyAddStudyButton = findViewById(R.id.applyStudyAddStudyButton);
        studyDateAddStudyEditText = findViewById(R.id.studyDateAddStudyEditText);
        doctorNameAddStudyEditText = findViewById(R.id.doctorNameAddStudyEditText);
        observationsMultiLineTextView = findViewById(R.id.observationsAddStudyEditText);
        patientNameAddStudyTextView = findViewById(R.id.patientNameAddStudyTextView);

        Intent intent = getIntent();
        name = intent.getStringExtra("name"); surname = intent.getStringExtra("surname");
        pesel = intent.getStringExtra("pesel"); token = intent.getStringExtra("token");
        patientNameAddStudyTextView.setText(name + " " + surname);
        applyStudyAddStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListOfStudiesForPatient();
            }
        });
    }

    private void goToListOfStudiesForPatient() {
        observations = observationsMultiLineTextView.getText().toString();

        String URL = "http://192.168.8.108:8080/api/doctor/study/add";
        final JSONObject jsonBody = new JSONObject();
//        final String name_n_surname = patientNameAddStudyTextView.getText().toString();

        try {
            jsonBody.put("patientPesel", pesel);
            jsonBody.put("observations", observations);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY", response.toString());
                        Intent intent = new Intent(getApplicationContext(),
                                PatientDataFromDocPointOfViewActivity.class);
                        intent.putExtra("observations", observations);
                        intent.putExtra("pesel", pesel);
                        intent.putExtra("name", name);
                        intent.putExtra("surname", surname);
                        intent.putExtra("token", token);

                        System.out.println("Token from AddStudyAct: ... " + token);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Dodano badanie pacjentowi "
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
//                params.put("name", name);
//                params.put("surname", surname);
//                params.put("patientPesel", pesel);
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

