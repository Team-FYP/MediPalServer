package lk.ac.mrt.cse.medipal.model;

public class ShareRequest {
    private String doctor_id;
    private String patient_id;
    private boolean status;

    public ShareRequest(String doctor_id, String patient_id, boolean status) {
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.status = status;
    }

    public ShareRequest(){}

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
