package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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

import static com.android.telm.MainActivity.ip;

public class ListOfPatientsActivity extends AppCompatActivity implements PatientRecyclerAdapter2.OnPatientListener{

    private Button arrowBackSearchForPatientButton;
    String token;

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Patient> patientList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_patients);

        mList = findViewById(R.id.patientsRecyclerAdminView);
        patientList = new ArrayList<>();
        getLoadPatients();

        adapter = new PatientRecyclerAdapter2(getApplicationContext(), patientList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

    }



    private void getLoadPatients() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String URL = "http://"+ip+":8080/api/admin/patient/listall";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Patient patient = new Patient();
                        if(jsonObject.getString("username").contains("user")) {
                            patient.setName(jsonObject.getString("username"));
//                        patient.setSurname(jsonObject.getString("surname"));
                            patient.setPesel("Pesel: " + jsonObject.getString("pesel"));
                            patientList.add(patient);

                        } else {
                        }

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
                return headers;
            }

        };
        queue.add(jsonArrayRequest);
        System.out.println(patientList.size());
    }

//    @Override
//    public void o(int position) {
        //        Patient patientClicked = patientList.get(position);
//        Intent intent = new Intent(getApplicationContext(), PatientDataFromDocPointOfViewActivity.class);
//        intent.putExtra("name", patientClicked.getName());
//        intent.putExtra("surname", patientClicked.getSurname());
//        intent.putExtra("pesel", patientClicked.getPesel());
//        intent.putExtra("token", token);
//        startActivity(intent);
//    }

    @Override
    public void onPatientClick(int position) {

    }
}