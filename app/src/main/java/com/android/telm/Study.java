package com.android.telm;

public class Study {

    private String observations, patientPesel, doctorName;


    public Study(String observations, String patientPesel) {
        this.observations = observations;
        this.patientPesel = patientPesel;
    }

    public Study() {

    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getPatientPesel() {
        return patientPesel;
    }

    public void setPatientPesel(String patientPesel) {
        this.patientPesel = patientPesel;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
