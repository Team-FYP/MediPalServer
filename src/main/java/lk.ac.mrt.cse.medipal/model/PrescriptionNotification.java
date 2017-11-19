package lk.ac.mrt.cse.medipal.model;

public class PrescriptionNotification {
    private String patient_id;
    private String doctor_id;
    private int prescription_id;
    private boolean status;
    private String message;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(int prescription_id) {
        this.prescription_id = prescription_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PrescriptionNotification(String patient_id, String doctor_id, int prescription_id, boolean status, String message){
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.prescription_id = prescription_id;
        this.status = status;
        this.message = message;
    }


}
