package lk.ac.mrt.cse.medipal.model;

public class PatientAllergy {
    private String patient_id;
    private int allergy_id;

    public PatientAllergy(String patient_id, int allergy_id) {
        this.patient_id = patient_id;
        this.allergy_id = allergy_id;
    }
    public PatientAllergy(){
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public int getAllergy_id() {
        return allergy_id;
    }

    public void setAllergy_id(int allergy_id) {
        this.allergy_id = allergy_id;
    }
}
