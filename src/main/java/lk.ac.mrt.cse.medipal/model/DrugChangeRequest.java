package lk.ac.mrt.cse.medipal.model;

import java.util.ArrayList;

public class DrugChangeRequest {
    private Drug new_drug;
    private ArrayList<Drug> drug_list;
    private String patient_id;
    private String disease_name;

    public Drug getNew_drug() {
        return new_drug;
    }

    public void setNew_drug(Drug new_drug) {
        this.new_drug = new_drug;
    }

    public ArrayList<Drug> getDrug_list() {
        return drug_list;
    }

    public void setDrug_list(ArrayList<Drug> drug_list) {
        this.drug_list = drug_list;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public DrugChangeRequest() {

    }

    public DrugChangeRequest(Drug new_drug, ArrayList<Drug> drug_list, String patient_id, String disease_name) {
        this.new_drug = new_drug;

        this.drug_list = drug_list;
        this.patient_id = patient_id;
        this.disease_name = disease_name;
    }
}
