package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientDataFromDocPointOfViewActivity extends AppCompatActivity {

    private Button goBackButton_PatientData, addStudyButton;
    private TextView actualPatientNameTextView, numberOfRecordsTextView;
    String token, namePatient, surnamePatient, patientPesel, doctorsPesel, doctorName, doctorSurname;
    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Study> studyList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data_from_doc_point_of_view);

        mList = findViewById(R.id.studyListFromDocRecyclerView);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        namePatient = intent.getStringExtra("name");
        surnamePatient = intent.getStringExtra("surname");
        patientPesel = intent.getStringExtra("pesel");

//        getDoctorInfoAsADoctor();

        studyList = new ArrayList<>();

        getListOfStudiesOfSinglePatient();

        adapter = new StudyRecyclerAdapterFromDocPointOfView(getApplicationContext(), studyList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        goBackButton_PatientData = findViewById(R.id.goBackButton);
        addStudyButton = findViewById(R.id.addStudyButton);
        actualPatientNameTextView = findViewById(R.id.actualPatientNameImageView);
        numberOfRecordsTextView = findViewById(R.id.numberOfRecordsTextView);

        addStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudyCreation();
            }
        });
        getPatientCardCreatedByDoctor();
//        getNumberOfStudiesForPatient();
    }

//    private List<String> getDoctorInfoAsADoctor(String doctorPesel)
//    {
//        String URL = "http://192.168.99.1:8080/api/doctor/info/" + doctorPesel;
//        List<String> nameSurnameListDoctor;
//
//        System.out.println(URL);
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
//                null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
////                String doctorName = null, doctorSurname;
//                Log.d("Response", response.toString());
////                actualPatientNameTextView.setText(name + " " + surname);
////                response.getString("name", doctorName);
////                response.getString("surname", doctorSurname);
//
//                Toast.makeText(getApplicationContext(), "Siema!", Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent intent = getIntent();
//                token = intent.getStringExtra("token");
//                System.out.println("PatientDataFromDocPointOfView token: " + token);
//                headers.put("Authorization", "Bearer " + token);
////                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.start();
//        queue.add(req);
//    }

    private void getListOfStudiesOfSinglePatient() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String URL = "http://192.168.99.1:8080/api/doctor/patient/" + patientPesel + "/liststudies";



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Study study = new Study();
                        study.setObservations(jsonObject.getString("observations"));
//                        List<String> docNameSurname = getDoctorInfoAsADoctor(jsonObject.getString("doctorPesel"));

//                        study.setDoctorName(docNameSurname.get(0) + " " + docNameSurname.get(1));



                        studyList.add(study);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

//    private void getNumberOfStudiesForPatient() {
//        String peselNew = pesel;
//
//        String URL = "http://192.168.99.1:8080/api/doctor/patient/" + peselNew;
//        System.out.println(URL);
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, URL,
//                null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Response", response.toString());
//                actualPatientNameTextView.setText(name + " " + surname);
//                Toast.makeText(getApplicationContext(), "Siema!", Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent intent = getIntent();
//                token = intent.getStringExtra("token");
//                System.out.println("PatientDataFromDocPointOfView token: " + token);
//                headers.put("Authorization", "Bearer " + token);
////                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.start();
//        queue.add(req);
//    }

    private void getPatientCardCreatedByDoctor() {

        String peselNew = patientPesel;

        String URL = "http://192.168.99.1:8080/api/doctor/patient/" + peselNew;
        System.out.println(URL);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                actualPatientNameTextView.setText(namePatient + " " + surnamePatient);
                Toast.makeText(getApplicationContext(), "Siema!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                Intent intent = getIntent();
                token = intent.getStringExtra("token");
                System.out.println("PatientDataFromDocPointOfView token: " + token);
                headers.put("Authorization", "Bearer " + token);
//                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        queue.add(req);
    }

    private void goToStudyCreation() {

        Intent intent = new Intent(this, AddStudyActivity.class);
        intent.putExtra("name", namePatient);
        intent.putExtra("surname", surnamePatient);
        intent.putExtra("pesel", patientPesel);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
