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

public class PatientDataFromDocPointOfViewActivity extends AppCompatActivity implements StudyRecyclerAdapterFromDocPointOfView.OnStudyListener {

    private Button goBackButton_PatientData, addStudyButton;
    private TextView actualPatientNameTextView, numberOfRecordsTextView;
    String token, namePatient, surnamePatient, patientPesel, doctorsPesel, dateOfStudy,
            nameOfDoctorFromStudy, surnameOfDoctorFromStudy, studyObservations, wholeNameOfDoctorFromStudy;
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

        //Intent from PreviousActivity
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        namePatient = intent.getStringExtra("name");
        surnamePatient = intent.getStringExtra("surname");
        patientPesel = intent.getStringExtra("pesel");
        studyObservations = intent.getStringExtra("observations");

        getListOfStudiesOfSinglePatient();

        studyList = new ArrayList<>();

        adapter = new StudyRecyclerAdapterFromDocPointOfView(getApplicationContext(), studyList, this);

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

//        adapter.notifyDataSetChanged();

        addStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudyCreation();
            }
        });
        getPatientCardCreatedByDoctor();
//        getNumberOfStudiesForPatient();
    }

    private String getNameAndSurnameOfTheDoctorFromStudyByPeselString (String name, String surname) {
        String wholeName;
        wholeName = name + " " + surname;
        return wholeName;
    }

    private void getNameAndSurnameOfDoctorFromStudy(String doctorsPesel) {

        String URL = "http://192.168.99.1:8080/api/doctor/info/" + doctorsPesel;
        System.out.println(doctorsPesel + " ten pesel nalezy do doktora ktory stworzyl badanie");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
//                actualPatientNameTextView.setText(namePatient + " " + surnamePatient);
                Toast.makeText(getApplicationContext(), "Siema!", Toast.LENGTH_SHORT).show();
                try {
                    nameOfDoctorFromStudy = response.getString("name");
                    surnameOfDoctorFromStudy = response.getString("surname");
                    wholeNameOfDoctorFromStudy = getNameAndSurnameOfTheDoctorFromStudyByPeselString(nameOfDoctorFromStudy, surnameOfDoctorFromStudy);

                    System.out.println(wholeNameOfDoctorFromStudy + " <- czy działa");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                return headers;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        queue.add(req);
//        System.out.println(wholeNameOfDoctorFromStudy + " <- czy działa");
    }

    private void getListOfStudiesOfSinglePatient() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String peselNew = patientPesel;
        System.out.println("Pesel Kowalskiego: " + peselNew);
        System.out.println("Token: " + token);
        System.out.println("Obserwacje: " + studyObservations);

        String URL = "http://192.168.99.1:8080/api/doctor/patient/" + peselNew + "/liststudies";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        doctorsPesel = jsonObject.getString("doctorPesel");
                        dateOfStudy = jsonObject.getString("dateOfStudy");
                        getNameAndSurnameOfDoctorFromStudy(doctorsPesel);

                        String observations = jsonObject.getString("observations");
                        Study study = new Study();
                        study.setObservations(observations);
                        study.setDoctorName(wholeNameOfDoctorFromStudy);
                        study.setStudyDateNTime(dateOfStudy);

                        studyList.add(study);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                numberOfRecordsTextView.setText(String.valueOf(studyList.size()));

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent intent = getIntent();
//            token = intent.getStringExtra("token");
                System.out.println("SearchForPatientToken: " + token);
                headers.put("Authorization", "Bearer " + token);
//                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

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

    @Override
    public void onStudyClick(int position) {
        Study studyClicked = studyList.get(position);
        Intent intent = new Intent(getApplicationContext(), StudyReviewActivity.class);
        intent.putExtra("observations", studyClicked.getObservations());
        intent.putExtra("doctorName", studyClicked.getDoctorName());
        intent.putExtra("date", studyClicked.getStudyDateNTime());
        intent.putExtra("name", namePatient);
        intent.putExtra("surname", surnamePatient);
        intent.putExtra("peselPatient", patientPesel);

        startActivity(intent);
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

}
