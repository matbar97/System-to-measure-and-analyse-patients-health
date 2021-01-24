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

public class ListOfDoctorsActivity extends AppCompatActivity implements DoctorsRecyclerAdapterAdminView.OnDoctorListener {

    private Button arrowBack;
    String token;

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Doctor> doctorList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_doctors);

        mList = findViewById(R.id.doctorsRecyclerAdminView);
        arrowBack = findViewById(R.id.goBackToMainMenuAdminBtn);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToAdminMenu();
            }
        });

        doctorList = new ArrayList<>();
        getLoadDoctors();

        adapter = new DoctorsRecyclerAdapterAdminView(getApplicationContext(), doctorList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

    }

    private void goBackToAdminMenu() {
        Intent intent = new Intent(getApplicationContext(), AdminMainMenuActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }


    private void getLoadDoctors() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String URL = "http://"+ip+":8080/api/admin/doctor/listall";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Doctor doctor = new Doctor();
                            doctor.setName(jsonObject.getString("name") + " " + jsonObject.getString("surname"));
                            doctor.setPesel("Pesel: " + jsonObject.getString("pesel"));
                            doctor.setId(jsonObject.getString("id"));
                            doctorList.add(doctor);
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
                Log.i("Volley Error: ", String.valueOf(error));
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
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onDocClick(int position) {
        Doctor doctorClicked = doctorList.get(position);
        Intent intent = new Intent(getApplicationContext(), DoctorDeleteActionActivity.class);
        intent.putExtra("username", doctorClicked.getName());
        intent.putExtra("token", token);
        intent.putExtra("id", doctorClicked.getId());
        startActivity(intent);
    }
}