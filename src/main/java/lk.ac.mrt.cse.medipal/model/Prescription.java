package lk.ac.mrt.cse.medipal.model;

import java.util.ArrayList;

public class Prescription {
    private int prescription_id;

    private ArrayList<PrescriptionDrug> prescription_drugs;
    private Doctor doctor;
    private Patient patient;
    private String disease_id;
    private String doctor_id;
    private String prescription_date;
    private PrescriptionAllergy prescriptionAllergy;


    public PrescriptionAllergy getPrescriptionAllergy() {
        return prescriptionAllergy;
    }

    public void setPrescriptionAllergy(PrescriptionAllergy prescriptionAllergy) {
        this.prescriptionAllergy = prescriptionAllergy;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public Prescription(int prescription_id, ArrayList<PrescriptionDrug> prescription_drugs, Doctor doctor, Patient patient, String disease_id, String doctor_id, String prescription_date) {
        this.prescription_id = prescription_id;
        this.prescription_drugs = prescription_drugs;
        this.doctor = doctor;
        this.patient = patient;
        this.disease_id = disease_id;
        this.doctor_id = doctor_id;
        this.prescription_date = prescription_date;
    }

    public String getPrescription_date() {
        return prescription_date;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setPrescription_date(String prescription_date) {
        this.prescription_date = prescription_date;
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

    public String getDisease_id() {
        return disease_id;
    }

    public int getPrescription_id() {
        return prescription_id;
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

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }

    public void setPrescription_id(int prescription_id) {

        this.prescription_id = prescription_id;
    }

    public Prescription(){
    }



}

