package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.AllergyNotification;
import lk.ac.mrt.cse.medipal.model.NotificationResponse;
import lk.ac.mrt.cse.medipal.model.PrescriptionNotification;
import lk.ac.mrt.cse.medipal.model.ShareNotification;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NotificationResponseController {

    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(NotificationResponseController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public NotificationResponse getNotificationResponse(String user_id){
        ShareNotificationController shareNotificationController = new ShareNotificationController();
        PrescriptionNotificationController prescriptionNotificationController = new PrescriptionNotificationController();
        AllergyNotificationController allergyNotificationController = new AllergyNotificationController();
        DoctorController doctorController = new DoctorController();
        NotificationResponse notificationResponse = new NotificationResponse();
        boolean isADoctor = doctorController.isADoctor(user_id);
        if(isADoctor){
            ArrayList<ShareNotification> shareRequestArrayList = shareNotificationController.getAllSharedNotifications(user_id);
            ArrayList<AllergyNotification> allergyNotificationArrayList = allergyNotificationController.getAllAllergyNotifications(user_id);
            notificationResponse.setAllergy_notifications(allergyNotificationArrayList);
            notificationResponse.setShare_notifications(shareRequestArrayList);
        }else {
            ArrayList<PrescriptionNotification> prescriptionNotificationArrayList = prescriptionNotificationController.getAllPrescriptionNotifications(user_id);
            notificationResponse.setPrescription_notifications(prescriptionNotificationArrayList);
        }
        return notificationResponse;

    }
}
