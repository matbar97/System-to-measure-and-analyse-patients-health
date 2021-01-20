package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

public class PatientCardDataFromPatientPointOfView extends AppCompatActivity  implements PatientCardRecyclerAdapter.OnStudyListener{

    private TextView patientNameTextViewPatientView, numberOfRecordsTextViewPatientView;
    String myNamePatient, mySurnamePatient, token, myPeselPatient, studyObservations, doctorsPesel, dateOfStudy;
    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Study> studyList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_card_data_from_patient_point_of_view);
        patientNameTextViewPatientView=findViewById(R.id.actualPatientNameTextViewPatientView);
        numberOfRecordsTextViewPatientView=findViewById(R.id.numberOfRecordsTextViewPatientView);

        Intent intent = getIntent();
        myNamePatient = intent.getStringExtra("myNamePatient");
        mySurnamePatient = intent.getStringExtra("mySurnamePatient");
        token = intent.getStringExtra("token");
        myPeselPatient = intent.getStringExtra("myPeselPatient");
        studyObservations = intent.getStringExtra("observations");

        mList = findViewById(R.id.studyListFromPatientViewRecyclerView);
        getListOfStudiesOfSinglePatientPatientView();
        patientNameTextViewPatientView.setText(myNamePatient + " " + mySurnamePatient);
        studyList = new ArrayList<>();

        adapter = new PatientCardRecyclerAdapter(getApplicationContext(), studyList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStudyClick(int position) {

        Intent i = new Intent(this, StudyReviewActivity.class);
        startActivity(i);
    }

    private void getListOfStudiesOfSinglePatientPatientView() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
//        String peselNew = patientPesel;
//        System.out.println("Pesel Kowalskiego: " + peselNew);
        System.out.println("Token: " + token);
        System.out.println("Obserwacje: " + studyObservations);

        String URL = "http://" + ip + ":8080/api/patient/study/list";
//        http://192.168.99.1:8080/api/patient/study/list
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        doctorsPesel = jsonObject.getString("doctorPesel");
                        dateOfStudy = jsonObject.getString("dateOfStudy");
                        System.out.println((doctorsPesel) + " to nas interesuje");

                        String observations = jsonObject.getString("observations");
                        Study study = new Study();
                        study.setObservations(observations);
//                        study.setDoctorName(getNameAndSurnameOfDoctorFromStudy(doctorsPesel));
                        study.setDoctorName("Mostowiak");

                        study.setStudyDateNTime(dateOfStudy);

                        studyList.add(study);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                numberOfRecordsTextViewPatientView.setText(String.valueOf(studyList.size()));

                adapter.notifyDataSetChanged();
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
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
}
