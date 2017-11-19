package lk.ac.mrt.cse.medipal.model;

public class ShareNotification {
    private String patient_id;
    private String doctor_id;
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

    public ShareNotification(String patient_id, String doctor_id, boolean status, String message) {
        this.patient_id = patient_id;

        this.doctor_id = doctor_id;
        this.status = status;
        this.message = message;
    }
}
