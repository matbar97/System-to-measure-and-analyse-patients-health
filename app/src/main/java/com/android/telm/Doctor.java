package com.android.telm;


class Doctor {
    public Doctor() {

    }

    private String name;
    private String surname;
    private String pesel;

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }

    public Doctor(String name, String surname, String pesel) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
    }

    public Doctor(Doctor Doctor) {
        this.name = Doctor.getName();
        this.pesel = Doctor.getPesel();
        this.surname = Doctor.getSurname();
    }

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

}