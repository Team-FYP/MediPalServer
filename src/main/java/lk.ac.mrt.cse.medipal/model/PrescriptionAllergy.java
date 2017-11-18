package lk.ac.mrt.cse.medipal.model;

public class PrescriptionAllergy {
    private Prescription prescription;
    private String patient_id;
    private Allergy allergy;
    private String severity;
    private String allergy_description;

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

    public PrescriptionAllergy(String patient_id, Allergy allergy, Prescription prescription, String severity, String allergy_description) {
        this.prescription = prescription;
        this.prescription = prescription;
        this.patient_id = patient_id;
        this.allergy = allergy;
        this.severity = severity;
        this.allergy_description = allergy_description;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public Allergy getAllergy() {
        return allergy;
    }

    public void setAllergy_id(Allergy allergy) {
        this.allergy = allergy;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
