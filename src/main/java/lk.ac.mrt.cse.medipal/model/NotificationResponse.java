package lk.ac.mrt.cse.medipal.model;

import java.util.ArrayList;

public class NotificationResponse {
    private ArrayList<ShareNotification> share_notifications;
    private ArrayList<AllergyNotification> allergy_notifications;
    private ArrayList<PrescriptionNotification> prescription_notifications;

    public NotificationResponse(ArrayList<ShareNotification> share_notifications, ArrayList<AllergyNotification> allergy_notifications, ArrayList<PrescriptionNotification> prescription_notifications) {
        this.share_notifications = share_notifications;
        this.allergy_notifications = allergy_notifications;
        this.prescription_notifications = prescription_notifications;
    }

    public NotificationResponse(){}

    public ArrayList<ShareNotification> getShare_notifications() {
        return share_notifications;
    }

    public void setShare_notifications(ArrayList<ShareNotification> share_notifications) {
        this.share_notifications = share_notifications;
    }

    public ArrayList<AllergyNotification> getAllergy_notifications() {
        return allergy_notifications;
    }

    public void setAllergy_notifications(ArrayList<AllergyNotification> allergy_notifications) {
        this.allergy_notifications = allergy_notifications;
    }

    public ArrayList<PrescriptionNotification> getPrescription_notifications() {
        return prescription_notifications;
    }

    public void setPrescription_notifications(ArrayList<PrescriptionNotification> prescription_notifications) {
        this.prescription_notifications = prescription_notifications;
    }
}
