package com.android.telm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class Patient {
    public Patient() {

    }

    private String name;
    private String surname;
    private String pesel;

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }

    public Patient(String name, String surname, String pesel) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
    }

    public Patient(Patient patient) {
        this.name = patient.getName();
        this.pesel = patient.getPesel();
        this.surname = patient.getSurname();
    }

//    public int getId() {
//        return id;
////    }
////
////    public void setId(int id) {
////        this.id = id;
////    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

//    public String packData() {
//        JSONObject jo=new JSONObject();
//        StringBuffer packedData=new StringBuffer();
//
//        try {
//            jo.put("name", name);
//            jo.put("surname", surname);
//            jo.put("pesel", pesel);
//            Boolean firstValue=true;
//            Iterator it=jo.keys();
//            do {
//                String key = it.next().toString();
//                String value = jo.get(key).toString();
//
//                if(firstValue) {
//                    firstValue=false;
//                } else {
//                    packedData.append("&");
//                }
//
//                packedData.append(URLEncoder.encode(key,"UTF-8"));
//                packedData.append("=");
//                packedData.append(URLEncoder.encode(value,"UTF-8"));
//
//            } while (it.hasNext());
//            {
//                return packedData.toString();
//            }
//
//        } catch (JSONException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
