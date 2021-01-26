package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.telm.MainActivity.ip;

public class EditStudyActivity extends AppCompatActivity {

    private Button backEditStudyButton, applyStudyEditStudyButton;
    private EditText observationsMultiLineTextView, dataBadania;
    private TextView patientNameAddStudyTextView;
    String name, surname, pesel, token, observations, doctorsName, dateOfStudy, dateOfRealStudy, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_study);
        backEditStudyButton = findViewById(R.id.backEditStudyButton);
        applyStudyEditStudyButton = findViewById(R.id.applyStudyEditStudyButton);
        observationsMultiLineTextView = findViewById(R.id.observationsEditStudyEditText);
        patientNameAddStudyTextView = findViewById(R.id.patientNameEditStudyTextView);
        dataBadania = findViewById(R.id.studyDateEditStudyEditText);
        dataBadania.setInputType(InputType.TYPE_NULL);

        dataBadania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dataBadania);
            }

            private void showDateTimeDialog(final EditText dataBadania) {
                final Calendar calendar = Calendar.getInstance();

                Locale.setDefault(Locale.forLanguageTag("PL"));
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Locale.setDefault(Locale.forLanguageTag("PL"));
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Locale.setDefault(Locale.forLanguageTag("PL"));
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                dataBadania.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        };
                        Locale.setDefault(Locale.forLanguageTag("PL"));
                        new TimePickerDialog(EditStudyActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();


                    }
                };
                Locale.setDefault(Locale.forLanguageTag("PL"));
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditStudyActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        pesel = intent.getStringExtra("peselPatient");
        token = intent.getStringExtra("token");
        doctorsName = intent.getStringExtra("doctorName");
        dateOfStudy = intent.getStringExtra("date");
        patientNameAddStudyTextView.setText(name + " " + surname);
        observations = intent.getStringExtra("observations");
        dateOfRealStudy = intent.getStringExtra("dateOfStudy");
        id = intent.getStringExtra("id");

        System.out.println(token);

        observationsMultiLineTextView.setText(observations);
        dataBadania.setText(dateOfRealStudy);


        applyStudyEditStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListOfStudiesOfPatientInDocView();
            }
        });

        backEditStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToStudyReview();
            }
        });
    }

    private void goBackToStudyReview() {
        onBackPressed();
    }

    private void goToListOfStudiesOfPatientInDocView() {
        observations = observationsMultiLineTextView.getText().toString();

        String URL = "http://" + ip + ":8080/api/doctor/patient/study/" + id;
        final JSONObject jsonBody = new JSONObject();
        try {
            if (!observations.isEmpty() && !dataBadania.getText().toString().isEmpty()) {
                jsonBody.put("patientPesel", pesel);
                jsonBody.put("dateAdded", dataBadania.getText().toString());
                jsonBody.put("observations", observations);

            } else {
                Toast.makeText(this, "Uzupełnij dane badania", Toast.LENGTH_SHORT).show();
                return;
            }
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
                        intent.putExtra("surname", surname);
                        intent.putExtra("name", name);
                        intent.putExtra("dateAdded",dataBadania.getText().toString());
                        intent.putExtra("token", token);
                        startActivity(intent);
                        finish();

                        Toast.makeText(getApplicationContext(), "Badanie dla pacjenta "
                                + name + " " + surname + " zostało zaktualizowane", Toast.LENGTH_SHORT).show();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Problem", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext()
                                , "Błąd edycji badania"
                                , Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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