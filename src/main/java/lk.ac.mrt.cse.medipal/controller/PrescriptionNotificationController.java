package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.PrescriptionNotification;
import lk.ac.mrt.cse.medipal.model.ShareNotification;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionNotificationController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionNotification.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public ArrayList<PrescriptionNotification> getAllPrescriptionNotifications(String patient_id){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `prescription_notification` WHERE patient_id=? ORDER BY id DESC";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patient_id);
            resultSet = preparedStatement.executeQuery();
            ArrayList<PrescriptionNotification> prescriptionNotificationArrayList = new ArrayList<>();
            PatientController patientController = new PatientController();
            DoctorController doctorController = new DoctorController();
            PrescriptionController prescriptionController = new PrescriptionController();
            while (resultSet.next()){
                PrescriptionNotification prescriptionNotification = new PrescriptionNotification();
                prescriptionNotification.setPatient(patientController.getPatiaentDetails(resultSet.getString("patient_id")));
                prescriptionNotification.setDoctor(doctorController.getDoctorDetails(resultSet.getString("doctor_id")));
                prescriptionNotification.setStatus(resultSet.getString("status"));
                prescriptionNotification.setMessage(resultSet.getString("message"));
                prescriptionNotification.setTime(String.valueOf(resultSet.getTimestamp("time")));
                prescriptionNotification.setPrescription(prescriptionController.getPrescriptionByID(resultSet.getInt("prescription_id")));
                prescriptionNotificationArrayList.add(prescriptionNotification);
            }
            return prescriptionNotificationArrayList;

        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting prescription notifications", ex);
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
