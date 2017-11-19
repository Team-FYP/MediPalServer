package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.ShareNotification;
import lk.ac.mrt.cse.medipal.model.ShareRequest;
import lk.ac.mrt.cse.medipal.util.MessageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ShareNotificationController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(ShareNotificationController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public boolean addShareHistoryNotification(ShareRequest shareRequest){
        boolean status=false;
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        PatientController patientController = new PatientController();
        String patient_name = patientController.getPatientNameByID(shareRequest.getPatient_id());
        String patient_gender = patientController.getPatientGenderByID(shareRequest.getPatient_id());
        try {

            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `share_notification` " +
                    " (`patient_id`,`doctor_id`,`status`, `message`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, shareRequest.getPatient_id());
            preparedStatement.setString(2, shareRequest.getDoctor_id());
            preparedStatement.setString(3, shareRequest.getStatus());
            preparedStatement.setString(4, MessageUtil.HistorySharedMessageBuild(patient_name, patient_gender));

            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error adding share notification", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return status;
    }

    public ArrayList<ShareNotification> getAllSharedNotifications(String doctor_id){
        try {
            DoctorController doctorController = new DoctorController();
            PatientController patientController = new PatientController();
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `share_notification` WHERE doctor_id=? ORDER BY `time` DESC";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            ArrayList<ShareNotification> shareNotificationArrayList = new ArrayList<>();
            while (resultSet.next()){
                ShareNotification shareNotification = new ShareNotification();
                shareNotification.setDoctor(doctorController.getDoctorDetails(resultSet.getString("doctor_id")));
                shareNotification.setPatient(patientController.getPatiaentDetails(resultSet.getString("patient_id")));
                shareNotification.setStatus(resultSet.getString("status"));
                shareNotification.setMessage(resultSet.getString("message"));
                shareNotification.setTime(String.valueOf(resultSet.getTimestamp("time")));
                shareNotificationArrayList.add(shareNotification);
            }
            return shareNotificationArrayList;

        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting medication history shared notifications", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return null;
    }


}
