package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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


public class MainActivity extends AppCompatActivity {
    public final static String ip = "192.168.99.1";
    private Button loginButton, registerButton;
    private EditText editTextLogin, editTextPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // RequestQueue For Handle Network Request

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPassword);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAccountCreation();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sendPostData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                goToDoctorsMainMenu();
            }
        });

    }

//    private void goToDoctorsMainMenu() {
//        Intent intent = new Intent(this, DoctorsMainMenuActivity.class);
//        intent.putExtra("username", editTextLogin.getText().toString());
//        intent.putExtra("password", editTextPassword.getText().toString());
//        startActivity(intent);
//    }

    private void goToAccountCreation() {
        Intent intent = new Intent(this, CreateDocAccountActivity.class);
        startActivity(intent);
    }

    public void sendPostData() throws JSONException {
        String URL = "http://"+ip+":8080/auth/signin";
        final JSONObject jsonBody = new JSONObject();
        final String username = editTextLogin.getText().toString();
        final String pwd = editTextPasswordLogin.getText().toString();

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
                            if (username.contains("doctor")){
                                Intent intent = new Intent(getApplicationContext(), DoctorsMainMenuActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Witaj " + username, Toast.LENGTH_SHORT).show();
                            }
                            else { //(username.contains("user")) {
                                Intent intent = new Intent(getApplicationContext(), PatientsMenuActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Witaj " + username, Toast.LENGTH_SHORT).show();
                            }
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

    }}

