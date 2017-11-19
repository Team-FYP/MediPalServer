package lk.ac.mrt.cse.medipal.model;

public class ShareNotification extends Notification{

    public ShareNotification(Patient patient, Doctor doctor, String message, String status, String time) {
        super(patient, doctor, message, status, time);
    }

    public ShareNotification(){}
}
