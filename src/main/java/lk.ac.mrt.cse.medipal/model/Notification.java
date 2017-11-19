package lk.ac.mrt.cse.medipal.model;

public abstract class Notification {
    private Patient patient;
    private Doctor doctor;
    private String message;
    private String status;
    private String time;

    public Notification(Patient patient, Doctor doctor, String message, String status, String time) {
        this.patient = patient;
        this.doctor = doctor;
        this.message = message;
        this.status = status;
        this.time = time;
    }

    public Notification(){}

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
