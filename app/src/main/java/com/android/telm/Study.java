package com.android.telm;

public class Study {

    private String observations;
    private String patientPesel;
    private String doctorName;
    private String doctorPesel;
    private String dateAdded;
    private String modified;

    public String getStudyDateNTime() {
        return studyDateNTime;
    }

    public void setStudyDateNTime(String studyDateNTime) {
        this.studyDateNTime = studyDateNTime;
    }

    private String studyDateNTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;


    public Study(String observations, String patientPesel, String dateAdded) {
        this.observations = observations;
        this.patientPesel = patientPesel;
        this.dateAdded = dateAdded;
    }

    public Study() {

    }


    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
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

    public String getDoctorPesel() {
        return doctorPesel;
    }

    public Study(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDoctorPesel(String doctorPesel) {
        this.doctorPesel = doctorPesel;
    }
}
