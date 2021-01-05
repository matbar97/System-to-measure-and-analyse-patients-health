package com.android.telm;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class Sender extends AsyncTask<Void, Void, String> {

    Context c;
    String urlAddress;
    EditText nameTxt, surnameTxt, peselTxt;

    String name, surname, pesel;

    ProgressDialog pd;


    public Sender(Context c, String urlAddress, EditText... editTexts) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.nameTxt = editTexts[0];
        this.surnameTxt = editTexts[2];
        this.peselTxt = editTexts[1];

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Tworzenie pacjenta");
        pd.setMessage("Dodawanie nowego pacjenta...");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return this.send();
    }

    private String send() {

        HttpURLConnection con = Connector.connect(urlAddress);

        if (con == null) {
            return null;
        }

        try {
            OutputStream os = con.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(new Patient(name, surname, pesel).packData());

            bw.flush();
            bw.close();
            os.close();

            int responseCode = con.getResponseCode();

            if (responseCode == con.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                String line = "";

                while ((line == br.readLine())) {
                    response.append(line);
                }

                br.close();

                return response.toString();
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.dismiss();

        if(s != null) {
            Toast.makeText(c,s,Toast.LENGTH_SHORT).show();

            nameTxt.setText("");
            surnameTxt.setText("");
            peselTxt.setText("");
        }
        else {
            Toast.makeText(c,"Nie udało się utworzyć pacjenta", Toast.LENGTH_SHORT).show();
        }
    }
}
