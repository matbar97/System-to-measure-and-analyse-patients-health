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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchForPatientActivity extends AppCompatActivity implements PatientRecyclerAdapter.OnPatientListener {

    private Button arrowBackSearchForPatientButton;
    private EditText searchForPatientEditText;

    String token;

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Patient> patientList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_patient);
        arrowBackSearchForPatientButton = findViewById(R.id.arrowBackSearchForPatientButton);
        searchForPatientEditText = findViewById(R.id.searchForPatientEditText);

        mList = findViewById(R.id.patientsRecyclerView);
        patientList = new ArrayList<>();
        getLoadPatients();

        adapter = new PatientRecyclerAdapter(getApplicationContext(), patientList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        searchForPatientEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String URL = "http://192.168.8.108:8080/api/doctor/listpatients/" + searchForPatientEditText.getText();
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                patientList.clear();
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Patient patient = new Patient();
                                patient.setName(jsonObject.getString("name"));
                                patient.setSurname(jsonObject.getString("surname"));
                                patient.setPesel(jsonObject.getString("pesel"));

                                patientList.add(patient);
                                System.out.println(patientList.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                        System.out.println(patientList.size());

                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Volley Error: ", String.valueOf(error));
                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        Intent intent = getIntent();
                        token = intent.getStringExtra("token");
                        System.out.println("SearchForPatientToken: " + token);
                        headers.put("Authorization", "Bearer " + token);
//                headers.put("Content-Type", "application/json");
                        return headers;
                    }

                };
                queue.add(jsonArrayRequest);
                System.out.println(patientList.size());
            }


        });

    }


    @Override
    public void onPatientClick(int position) {
        Patient patientClicked = patientList.get(position);
        Toast.makeText(getApplicationContext(), "Kliknieto w pacjenta: " + patientClicked.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), PatientDataFromDocPointOfViewActivity.class);
        intent.putExtra("name", patientClicked.getName());
        intent.putExtra("surname", patientClicked.getSurname());
        intent.putExtra("pesel", patientClicked.getPesel());
        intent.putExtra("token", token);
        startActivity(intent);
    }


    private void getLoadPatients() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL = "http://192.168.8.108:8080/api/doctor/listpatients";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Patient patient = new Patient();
                        patient.setName(jsonObject.getString("name"));
                        patient.setSurname(jsonObject.getString("surname"));
                        patient.setPesel(jsonObject.getString("pesel"));

                        patientList.add(patient);
                        System.out.println(patientList.size());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                System.out.println(patientList.size());

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", String.valueOf(error));
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Intent intent = getIntent();
                token = intent.getStringExtra("token");
                System.out.println("SearchForPatientToken: " + token);
                headers.put("Authorization", "Bearer " + token);
//                headers.put("Content-Type", "application/json");
                return headers;
            }

        };
        queue.add(jsonArrayRequest);
        System.out.println(patientList.size());
    }


}
