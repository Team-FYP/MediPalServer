package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.constants.Constants;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionAllergy;
import lk.ac.mrt.cse.medipal.util.Encryptor;
import lk.ac.mrt.cse.medipal.util.ImageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class PrescriptionAllergyController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionAllergyController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public ArrayList<PrescriptionAllergy> getPrescriptionAllergiesByPatient(String patient_id){
//        try {
//            connection = DB_Connection.getDBConnection().getConnection();
//            String SQL = "SELECT prescription_has_allergy.ALLERGY_ALLERGY_ID, prescription_has_allergy.PRESCRIPTION_PRESCRIPTION_ID, prescription_has_allergy.PRESCRIPTION_PATIENT_NIC, prescription_has_allergy.SEVERITY_NAME, prescription_has_allergy.ALLERGY_DESCRIPTION " +
//                    "FROM  prescription_has_allergy " +
//                    "INNER JOIN allergy ON allergy.ALLERGY_ID=prescription_has_allergy.ALLERGY_ALLERGY_ID AND prescription_has_allergy.PRESCRIPTION_PATIENT_NIC=?";
//            preparedStatement = connection.prepareStatement(SQL);
//            preparedStatement.setString(1, patient_id);
//            resultSet = preparedStatement.executeQuery();
//            ArrayList<PrescriptionAllergy> patientAllergyList = new ArrayList<>();
//            while (resultSet.next()){
//                PrescriptionAllergy patient_allergy = new PrescriptionAllergy();
//                patient_allergy.setAllergy_id(resultSet.getInt("ALLERGY_ALLERGY_ID"));
//                patient_allergy.setPatient_id(resultSet.getString("PRESCRIPTION_PATIENT_NIC"));
//                patient_allergy.setPrescription_id(resultSet.getInt("PRESCRIPTION_PRESCRIPTION_ID"));
//                patient_allergy.setSeverity(resultSet.getString("SEVERITY_NAME"));
//                patient_allergy.setAllergy_description(resultSet.getString("ALLERGY_DESCRIPTION"));
//                patientAllergyList.add(patient_allergy);
//            }
//            return patientAllergyList;
//        } catch (SQLException | IOException | PropertyVetoException ex) {
//            LOGGER.error("Error getting patient's prescription allergy details", ex);
//        } finally {
//            try {
//                DbUtils.closeQuietly(resultSet);
//                DbUtils.closeQuietly(preparedStatement);
//                DbUtils.close(connection);
//            } catch (SQLException ex) {
//                LOGGER.error("Error closing sql connection", ex);
//            }
//        }
        return null;
    }

    public boolean addPrescriptionAllergy(PrescriptionAllergy prescriptionAllergy){
        boolean status=false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `prescription_has_allergy` " +
                    " (`PRESCRIPTION_PRESCRIPTION_ID`,`PRESCRIPTION_PATIENT_NIC`,`SEVERITY_NAME`,`ALLERGY_DESCRIPTION`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setInt(1, prescriptionAllergy.getPrescription().getPrescription_id());
            preparedStatement.setString(2, prescriptionAllergy.getPatient_id());
            preparedStatement.setString(3, prescriptionAllergy.getSeverity());
            preparedStatement.setString(4, prescriptionAllergy.getAllergy_description());

            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error adding allergy for prescription", ex);
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

    public Prescription checkForPrescriptionAllergy(Prescription prescription){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "SELECT * FROM prescription_has_allergy WHERE PRESCRIPTION_PRESCRIPTION_ID=?";
            PrescriptionAllergy prescriptionAllergy =new PrescriptionAllergy();
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setInt(1, prescription.getPrescription_id());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                prescriptionAllergy.setPatient_id(resultSet.getString("PRESCRIPTION_PATIENT_NIC"));
                prescriptionAllergy.setPrescription(prescription);
                prescriptionAllergy.setSeverity(resultSet.getString("SEVERITY_NAME"));
                prescriptionAllergy.setAllergy_description(resultSet.getString("ALLERGY_DESCRIPTION"));
            }
            prescription.setPrescriptionAllergy(prescriptionAllergy);
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error checking patient has shared history with doctor patient ", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return prescription;
    }

    public PrescriptionAllergy getPrescriptionAllergyByID(int prescription_allergy_id){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `prescription_has_allergy` WHERE `prescription_allergy_id` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, prescription_allergy_id);
            resultSet = preparedStatement.executeQuery();
            PrescriptionAllergy prescriptionAllergy = new PrescriptionAllergy();
            PrescriptionController prescriptionController = new PrescriptionController();
            if (resultSet.next()){
                prescriptionAllergy.setPrescription(prescriptionController.getPrescriptionByID(resultSet.getInt("PRESCRIPTION_PRESCRIPTION_ID")));
                prescriptionAllergy.setPatient_id(resultSet.getString("PRESCRIPTION_PATIENT_NIC"));
                prescriptionAllergy.setSeverity(resultSet.getString("SEVERITY_NAME"));
                prescriptionAllergy.setAllergy_description(resultSet.getString("ALLERGY_DESCRIPTION"));
                return prescriptionAllergy;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting prescription allergy by ID", ex);
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

    public int getLastInsertedPrescriptionAllergyID(){
        int prescription_allergy_id = 0;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "SELECT prescription_allergy_id FROM `prescription_has_allergy` ORDER BY prescription_allergy_id DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(SQL1);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                prescription_allergy_id = resultSet.getInt("prescription_allergy_id");
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting last prescription allergy id", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return prescription_allergy_id;
    }
}
