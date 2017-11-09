package lk.ac.mrt.cse.medipal.model;

public class PrescriptionAllergy {
    private int prescription_id;
    private String patient_id;
    private int allergy_id;
    private String severity;

    public PrescriptionAllergy() {

    }

    public PrescriptionAllergy(int prescription_id, String patient_id, int allergy_id, String severity) {
        this.prescription_id = prescription_id;
        this.patient_id = patient_id;
        this.allergy_id = allergy_id;
        this.severity = severity;
    }

    public int getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(int prescription_id) {
        this.prescription_id = prescription_id;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
