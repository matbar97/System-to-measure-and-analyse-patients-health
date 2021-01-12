package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button loginButton, registerButton;
    private EditText editTextLogin, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // RequestQueue For Handle Network Request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAccountCreation();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostData();
                goToDoctorsMainMenu();
            }
        });

    }

    private void goToDoctorsMainMenu() {
        Intent intent = new Intent(this, DoctorsMainMenuActivity.class);
        startActivity(intent);
    }

    private void goToAccountCreation() {
        Intent intent = new Intent(this, CreateDocAccountActivity.class);
        startActivity(intent);
    }

    public void sendPostData(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://212.180.235.174:8080/auth/signin";
        final Context context = getApplicationContext();
        final String textTrue = "Request MSG";
        final String textFalse = "Request MSG - Failed";
        final int duration = Toast.LENGTH_LONG;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast toast = Toast.makeText(getApplicationContext(), "Request MSG", Toast.LENGTH_LONG);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Request MSG - False", Toast.LENGTH_LONG);
                toast.show();
                }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", editTextLogin.getText().toString());
                params.put("password", editTextPassword.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };

        queue.add(stringRequest);
    }

//    void sendLoginPost()  {
////        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
////        JSONObject object = new JSONObject();
////        try {
////            //input your API parameters
////            object.put("username",editTextLogin.getText().toString());
////            object.put("password",editTextPassword.getText().toString());
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        // Enter the correct url for your api service site
////        String url = "http://localhost:8080/auth/signin"; //getResources().getString(R.string.url);
////        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
////                new Response.Listener<JSONObject>() {
////                    @Override
////                    public void onResponse(JSONObject response) {
////                        System.out.println("String Response : "+ response.toString());
////                    }
////                }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                System.err.println("Error getting response");
////            }
////        });
////        requestQueue.add(jsonObjectRequest);
////
////    }
}

//        URL url = new URL ("https://twojwynik.postman.co/workspace/Team-Workspace~2789bd81-5908-47d0-881f-0f6be2b1fc90/request/14044193-ec8faf89-ec43-4c29-a19f-193f9038b498");
//        HttpURLConnection con = (HttpURLConnection)url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/json; utf-8");
//        con.setRequestProperty("Accept", "application/json");
//        con.setDoOutput(true);
//        String jsonInputString = "{" + "username:" + editTextLogin.getText().toString()+
//                "\n" + "password:" + editTextPassword.getText().toString() +"}";
//        try(OutputStream os = con.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        try(BufferedReader br = new BufferedReader(
//                new InputStreamReader(con.getInputStream(), "utf-8"))) {
//            StringBuilder response = new StringBuilder();
//            String responseLine = null;
//            while ((responseLine = br.readLine()) != null) {
//                response.append(responseLine.trim());
//            }
//            System.out.println(response.toString());
//        }
//
//        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String URL = "http://...";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("Title", "Android Volley Demo");
//            jsonBody.put("Author", "BNK");
//            final String requestBody = jsonBody.toString();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("VOLLEY", response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("VOLLEY", error.toString());
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//                @Override
//                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
//            };
//
//            requestQueue.add(stringRequest);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }