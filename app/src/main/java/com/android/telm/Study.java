package com.android.telm;

public class Study {

    private String observations;
    private String patientPesel;
    private String doctorName;

    public String getStudyDateNTime() {
        return studyDateNTime;
    }

    public void setStudyDateNTime(String studyDateNTime) {
        this.studyDateNTime = studyDateNTime;
    }

    private String studyDateNTime;


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
