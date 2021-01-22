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
import static com.android.telm.MainActivity.replace;

public class PatientCardDataFromPatientPointOfView extends AppCompatActivity  implements PatientCardRecyclerAdapter.OnStudyListener{

    private TextView patientNameTextViewPatientView, numberOfRecordsTextViewPatientView;
    private Button goBackToPatientMainMenuButton;
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
        goBackToPatientMainMenuButton = findViewById(R.id.goBackButton_PatientData_PatientView);

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

        goBackToPatientMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToPatientsMainMenu();
            }
        });
    }

    private void goBackToPatientsMainMenu() {
        onBackPressed();
    }

    @Override
    public void onStudyClick(int position) {
        Study studyClicked = studyList.get(position);
        Intent intent = new Intent(getApplicationContext(), StudyReviewActivity.class);
        intent.putExtra("observations", studyClicked.getObservations());
        intent.putExtra("doctorName", studyClicked.getDoctorName());
        intent.putExtra("date", studyClicked.getStudyDateNTime());
        intent.putExtra("name", myNamePatient);
        intent.putExtra("surname", mySurnamePatient);
        intent.putExtra("peselPatient", myPeselPatient);

        startActivity(intent);

    }

    private void getListOfStudiesOfSinglePatientPatientView() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL = "http://" + ip + ":8080/api/patient/study/list";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        doctorsPesel = jsonObject.getString("nameAndSurname");
                        dateOfStudy = jsonObject.getString("dateOfStudy");
                        String studyObservationsJSON = jsonObject.getString("observations");
                        Study study = new Study();
                        studyObservations = studyObservationsJSON;
                        study.setObservations("Obserwacje: " + studyObservations);
                        study.setDoctorName("Badał: dr " + doctorsPesel);
                        dateOfStudy = replace(dateOfStudy, 10, ' ');
                        dateOfStudy = dateOfStudy.substring(0,dateOfStudy.indexOf("."));
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
                System.out.println("SearchForPatientToken: " + token);
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
}
