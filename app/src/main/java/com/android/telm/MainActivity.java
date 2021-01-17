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
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
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
        String URL = "http://192.168.1.11:8080/auth/signin";
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

    }


    //        final JSONObject jsonBody = new JSONObject();
//        jsonBody.put("username", editTextLogin.getText().toString());
//        jsonBody.put("password", editTextPassword.getText().toString());
//        final String requestBody = jsonBody.toString();
//        // Use HttpURLConnection as the HTTP client
//        // Setup 1 MB disk-based cache for Volley
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.i("VOLLEY", response.toString());
//
//                try {
//                    if(jsonBody.getJSONObject("username").toString().isEmpty()) {
//                        Intent intent = new Intent(getApplicationContext(), DoctorsMainMenuActivity.class);
//                        intent.putExtra("username", editTextLogin.getText().toString());
//                        intent.putExtra("password", editTextPassword.getText().toString());
//                        intent.putExtra("token", "23123");
//                        startActivity(intent);
////                        Toast toast = Toast.makeText(getApplicationContext(), "Witaj w aplikacji Doktorze", Toast.LENGTH_LONG);
////                        toast.show();
//                        Toast.makeText(getApplicationContext(), "" + response.toString(), Toast.LENGTH_SHORT).show();
//
//                    }
//                    else if(jsonBody.getString("username").contains("user")) {
//                        Intent intent2 = new Intent(getApplicationContext(), PatientsMenuActivity.class);
//                        intent2.putExtra("username", editTextLogin.getText().toString());
//                        intent2.putExtra("password", editTextPassword.getText().toString());
//                        startActivity(intent2);
//                        Toast toast2 = Toast.makeText(getApplicationContext(), "Witaj w aplikacji Pacjencie", Toast.LENGTH_LONG);
//                        toast2.show();
//                    }
////                        else {
////                            Toast.makeText(getApplicationContext(),
////                                    "Wprowadź właściwe dane", Toast.LENGTH_SHORT).show();
////                        }
//                    } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VOLLEY", error.toString());
//                Toast toast = Toast.makeText(getApplicationContext(), "Błąd autoryzacji", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//
//            @Override
//            public byte[] getBody() {
//                try {
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }

//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                JSONObject responseString = "";
//                if (response != null) {
//                    responseString = String.valueOf(response.statusCode);
//                    // can get more details such as response.headers
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response.toString()));
//            }


//    public static boolean hasActiveInternetConnection(Context context) {
//        if (isNetworkAvailable(context)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//                urlc.setRequestProperty("User-Agent", "Test");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(1500);
//                urlc.connect();
//                return (urlc.getResponseCode() == 200);
//            } catch (IOException e) {
//                Log.e("Ok", "Error checking internet connection", e);
//            }
//        } else {
//            Log.d("Not ok", "No network available!");
//        }
//        return false;
//    }
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


//    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        String url = "http://192.168.78.1:8080/auth/signin";
//        final Context context = getApplicationContext();
//        final String textTrue = "Request MSG";
//        final String textFalse = "Request MSG - Failed";
//        final int duration = Toast.LENGTH_LONG;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Request MSG", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Request MSG - False", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("username", editTextLogin.getText().toString());
//                params.put("password", editTextPassword.getText().toString());
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/json");
//                return params;
//            }
//        };
//
//        queue.add(stringRequest);