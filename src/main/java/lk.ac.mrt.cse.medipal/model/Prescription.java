package lk.ac.mrt.cse.medipal.model;

import java.util.ArrayList;

public class Prescription {
    private String prescription_id;
    private ArrayList<PrescriptionDrug> prescription_drugs;
    private Doctor doctor;
    private Patient patient;
    private String disease;
    private String doctor_id;

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPrescription_id() {
        return prescription_id;
    }

    public ArrayList<PrescriptionDrug> getPrescription_drugs() {
        return prescription_drugs;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getDisease() {
        return disease;
    }

    public void setPrescription_id(String prescription_id) {

        this.prescription_id = prescription_id;
    }

    public void setPrescription_drugs(ArrayList<PrescriptionDrug> prescription_drugs) {
        this.prescription_drugs = prescription_drugs;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }


    public Prescription(String prescription_id, ArrayList<PrescriptionDrug> prescription_drugs, Doctor doctor, Patient patient, String disease, String doctor_id) {
        this.prescription_id = prescription_id;
        this.prescription_drugs = prescription_drugs;
        this.doctor = doctor;
        this.patient = patient;
        this.disease = disease;
        this.doctor_id = doctor_id;
    }

    public Prescription(){
    }



}

