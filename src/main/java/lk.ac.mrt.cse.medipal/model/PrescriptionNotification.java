package lk.ac.mrt.cse.medipal.model;

public class PrescriptionNotification extends Notification{
    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    private Prescription prescription;

    public PrescriptionNotification(Patient patient, Doctor doctor, String message, String status, String time, Prescription prescription) {
        super(patient, doctor, message, status, time);
        this.prescription = prescription;
    }

    public PrescriptionNotification(Prescription prescription) {
        this.prescription = prescription;
    }

    public PrescriptionNotification(){}
}
