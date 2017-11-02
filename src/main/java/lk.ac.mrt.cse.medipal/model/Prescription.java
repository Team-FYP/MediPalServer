package lk.ac.mrt.cse.medipal.model;

public class Prescription {
    private int prescription_id;
    private String date;
    private String patient_nic;
    private int disease_id;

    public Prescription(int prescription_id, String date, String patient_nic, int disease_id) {
        this.prescription_id = prescription_id;
        this.date = date;
        this.patient_nic = patient_nic;
        this.disease_id = disease_id;
    }

    public Prescription(){
    }

    public int getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(int disease_id) {
        this.disease_id = disease_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatient_nic() {
        return patient_nic;
    }

    public void setPatient_nic(String patient_nic) {
        this.patient_nic = patient_nic;
    }

    public int getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(int prescription_id) {
        this.prescription_id = prescription_id;
    }
}

