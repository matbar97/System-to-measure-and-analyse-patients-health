package com.android.telm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PatientCreationActivity extends AppCompatActivity {

    private Button addPatientButton, goToPreviousActivityButton;
    private EditText nameEditText, surnameEditText, peselEditText;
    private String urlAddr = "http://localhost:8080/api/patients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_creation);
        addPatientButton = findViewById(R.id.addPatientButton);
        goToPreviousActivityButton = findViewById(R.id.goToPreviousActivityButton);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        peselEditText = findViewById(R.id.peselEditText);
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sender s = new Sender(PatientCreationActivity.this, urlAddr, nameEditText, peselEditText, surnameEditText);
                s.execute();
                goToPatientsData();
            }
        });
    }

    private void goToPatientsData() {
        Intent intent = new Intent(this, PatientDataFromDocPointOfViewActivity.class);
        startActivity(intent);
    }
}

//class SendJsonDataToServer extends AsyncTask<String, String, String> {
//
//    @Override
//    protected String doInBackground(Patient... params) {
//        String JsonResponse = null;
//        Patient JsonDATA = params[0];
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        try {
//            URL url = new URL("x");
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            // is output buffer writter
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/json");
//            urlConnection.setRequestProperty("Accept", "application/json");
////set headers and method
//            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
//            writer.write(JsonDATA);
//// json data
//            writer.close();
//            InputStream inputStream = urlConnection.getInputStream();
////input stream
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String inputLine;
//            while ((inputLine = reader.readLine()) != null)
//                buffer.append(inputLine + "\n");
//            if (buffer.length() == 0) {
//                // Stream was empty. No point in parsing.
//                return null;
//            }
//            JsonResponse = buffer.toString();
////response data
//            Log.i("Good", JsonResponse);
//            //send to post execute
//            return JsonResponse;
////                return null;
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("Good", "Error closing stream", e);
//                }
//            }
//        }
//        return null;
//    }
//
//
//    @Override
//    protected void onPostExecute(String s) {
//
//    }
//}
//
//    public void senddatatoserver() {
//        //function in the activity that corresponds to the layout button
//        name = nameEditText.getText().toString();
//        surname = surnameEditText.getText().toString();
//        pesel = peselEditText.getText().toString();
//        JSONObject post_dict = new JSONObject();
//
//        try {
//            post_dict.put("name", name);
//            post_dict.put("surname", surname);
//            post_dict.put("pesel", pesel);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (post_dict.length() > 0) {
////            SendJsonDataToServer sjdts = new SendJsonDataToServer();
//            new SendJsonDataToServer().execute(String.valueOf(post_dict));
////            call to async class
//        }
