package lk.ac.mrt.cse.medipal.model;

public class PrescriptionAllergy {
    private Prescription prescription;
    private String patient_id;
    private String severity;
    private String allergy_description;
    private int prescription_allergy_id;

    public int getPrescription_allergy_id() {
        return prescription_allergy_id;
    }

    public void setPrescription_allergy_id(int prescription_allergy_id) {
        this.prescription_allergy_id = prescription_allergy_id;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription_id(Prescription prescription) {
        this.prescription = prescription;
    }

    public String getAllergy_description() {
        return allergy_description;
    }

    public void setAllergy_description(String allergy_description) {
        this.allergy_description = allergy_description;
    }

    public PrescriptionAllergy() {

    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public PrescriptionAllergy(String patient_id, Prescription prescription, String severity, String allergy_description) {
        this.prescription = prescription;
        this.patient_id = patient_id;
        this.severity = severity;
        this.allergy_description = allergy_description;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
