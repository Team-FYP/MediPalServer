package lk.ac.mrt.cse.medipal.controller;

import com.google.gson.JsonObject;
import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.constants.Constants;
import lk.ac.mrt.cse.medipal.constants.DB_constants;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.util.Encryptor;
import lk.ac.mrt.cse.medipal.util.ImageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakshan on 9/19/17.
 */
public class PatientController {

    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PatientController.class);


    public boolean savePatient(Patient patient){
        boolean status = false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `patient` " +
                    " (`NIC`,`PATIENT_NAME`,`GENDER`,`EMAIL`,`BIRTHDAY`,`CONTACT_NUMBER`,`EMERGENCY_CONTACT_NUMBER`,`PASSWORD`,`PROFILE_PICTURE`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, patient.getNic());
            preparedStatement.setString(2, patient.getName());
            preparedStatement.setString(3, patient.getGender());
            preparedStatement.setString(4, patient.getEmail());
            preparedStatement.setString(5, patient.getBirthday());
            preparedStatement.setString(6, patient.getMobile());
            preparedStatement.setString(7, patient.getEmergency_contact());
            preparedStatement.setString(8, Encryptor.encryptMD5(patient.getPassword()));
            preparedStatement.setString(9, ImageUtil.stringtoImage(patient.getImage(),Constants.PROFILE_PIC_PREFIX+patient.getNic(), Constants.FILE_FORMAT_JPG));

            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error saving Patient", ex);
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

    public boolean checkLogin(String username, String password){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `patient` WHERE `NIC` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String hash = resultSet.getString(DB_constants.Patient.PASSWORD);
                return Encryptor.verifyPassword(password,hash);
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error patient login", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return false;
    }

    public Patient getPatiaentDetails(String username){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `patient` WHERE `NIC` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            Patient patient = new Patient();
            if (resultSet.next()){
                patient.setNic(resultSet.getString("NIC"));
                patient.setName(resultSet.getString("PATIENT_NAME"));
                patient.setGender(resultSet.getString("GENDER"));
                patient.setEmail(resultSet.getString("EMAIL"));
                patient.setBirthday(resultSet.getString("BIRTHDAY"));
                patient.setMobile(resultSet.getString("CONTACT_NUMBER"));
                patient.setEmergency_contact(resultSet.getString("EMERGENCY_CONTACT_NUMBER"));
                patient.setImage(resultSet.getString("PROFILE_PICTURE"));
                return patient;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting patient details", ex);
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
