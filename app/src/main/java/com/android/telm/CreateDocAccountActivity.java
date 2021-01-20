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

import static com.android.telm.MainActivity.ip;

public class CreateDocAccountActivity extends AppCompatActivity {
    private Button applyButton, goBackButton;
    private EditText editTextName, editTextPESEL,
            editTextEmail, editTextPassword, editTextSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doc_account);
        applyButton = findViewById(R.id.applyButton);
        goBackButton = findViewById(R.id.goBackButton);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextName2);
        editTextPESEL = findViewById(R.id.editTextPESEL);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPasswordNewPatient);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    registerPatientPostData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//Listeners
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainScreen();
            }
        });
    }

    private void goBackToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerPatientPostData() throws JSONException {

        String URL = "http://"+ip+":8080/auth/register/add";
        final JSONObject jsonBody = new JSONObject();
        final String name = editTextName.getText().toString();
        final String surname = editTextSurname.getText().toString();
        final String pesel = editTextPESEL.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String username = editTextEmail.getText().toString();

        jsonBody.put("name", name);
        jsonBody.put("surname", surname);
        jsonBody.put("pesel", pesel);
        jsonBody.put("password", password);
        jsonBody.put("username", username);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY", response.toString());
                        //                            String token = response.getString("token");
//                        Intent intent = new Intent(getApplicationContext(), PatientsMenuActivity.class);
////                            intent.putExtra("token", token);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Zarejestrowano " + username, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Problem", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext()
                                , "Błąd autoryzacji."
                                , Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("name", name);
                params.put("surname", surname);
                params.put("pesel", pesel);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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
        sendLoginPostData();

    }

    public void sendLoginPostData() throws JSONException {

        String URL = "http://"+ip+":8080/auth/signin";
        final JSONObject jsonBody = new JSONObject();
        final String username = editTextName.getText().toString();
        final String pwd = editTextPassword.getText().toString();

        jsonBody.put("username", username);
        jsonBody.put("password", pwd);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY", response.toString());
                        try {
                            String token = response.getString("token");
//                            if (username.contains("doctor")){
//                                Intent intent = new Intent(getApplicationContext(), DoctorsMainMenuActivity.class);
//                                intent.putExtra("token", token);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Witaj " + username, Toast.LENGTH_SHORT).show();
//                            } else  {
                                Intent intent = new Intent(getApplicationContext(), PatientsMenuActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Witaj " + username, Toast.LENGTH_SHORT).show();
//                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Problem", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext()
                                ,"Nie ma Cię w systemie. Zarejestruj się lub wpisz właściwe dane"
                                ,Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", pwd);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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
