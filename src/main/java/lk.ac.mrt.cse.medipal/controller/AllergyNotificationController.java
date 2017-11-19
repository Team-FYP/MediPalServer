package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.AllergyNotification;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionAllergy;
import lk.ac.mrt.cse.medipal.model.PrescriptionNotification;
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

public class AllergyNotificationController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionNotification.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public ArrayList<AllergyNotification> getAllPrescriptionNotifications(String doctor_id){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `allergy_notification` WHERE doctor_id=? ORDER BY id DESC";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            ArrayList<AllergyNotification> allergyNotificationArrayList = new ArrayList<>();
            PatientController patientController = new PatientController();
            DoctorController doctorController = new DoctorController();
            PrescriptionController prescriptionController = new PrescriptionController();
            PrescriptionAllergyController prescriptionAllergyController = new PrescriptionAllergyController();
            while (resultSet.next()){
                AllergyNotification allergyNotification = new AllergyNotification();
                allergyNotification.setPatient(patientController.getPatiaentDetails(resultSet.getString("patient_id")));
                allergyNotification.setDoctor(doctorController.getDoctorDetails(resultSet.getString("doctor_id")));
                allergyNotification.setStatus(resultSet.getString("status"));
                allergyNotification.setMessage(resultSet.getString("message"));
                allergyNotification.setTime(String.valueOf(resultSet.getTimestamp("time")));
                allergyNotification.setPrescription(prescriptionController.getPrescriptionByID(resultSet.getInt("prescription_id")));
                allergyNotification.setPrescriptionAllergy(prescriptionAllergyController.getPrescriptionAllergyByID(resultSet.getInt("prescription_allergy_id")));
                allergyNotificationArrayList.add(allergyNotification);
            }
            return allergyNotificationArrayList;

        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting allergy notifications", ex);
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

    public boolean addPrescriptionAllergyNotification(PrescriptionAllergy prescriptionAllergy){
        boolean status=false;
        DoctorController doctorController = new DoctorController();
        PatientController patientController = new PatientController();
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        String patient_name = patientController.getPatientNameByID(prescriptionAllergy.getPatient_id());
        try {

            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `allergy_notification` " +
                    " (`patient_id`,`doctor_id`, `prescription_id`, `message`, `prescription_allergy_id`) " +
                    "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, prescriptionAllergy.getPatient_id());
            preparedStatement.setString(2, prescriptionAllergy.getPrescription().getDoctor_id());
            preparedStatement.setInt(3, prescriptionAllergy.getPrescription().getPrescription_id());
            preparedStatement.setString(4, MessageUtil.AllergyAddedMessageBuild(patient_name));
            preparedStatement.setInt(5, prescriptionAllergy.getPrescription_allergy_id());
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error adding prescription notification", ex);
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
}
