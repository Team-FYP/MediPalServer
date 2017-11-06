package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.constants.Constants;
import lk.ac.mrt.cse.medipal.constants.DB_constants;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.util.Encryptor;
import lk.ac.mrt.cse.medipal.util.ImageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import javax.print.Doc;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DoctorController.class);


    public boolean saveDoctor(Doctor doctor){
        boolean status = false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `doctor` " +
                    " (`REGISTRATION_NO`,`SPECIALITY`,`NAME`,`GENDER`,`EMAIL`,`CONTACT_NUMBER`,`PASSWORD`,`PROFILE_PICTURE`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, doctor.getRegistration_id());
            preparedStatement.setString(2, doctor.getSpeciality());
            preparedStatement.setString(3, doctor.getName());
            preparedStatement.setString(4, doctor.getGender());
            preparedStatement.setString(5, doctor.getEmail());
            preparedStatement.setString(6, doctor.getMobile());
            preparedStatement.setString(7, Encryptor.encryptMD5(doctor.getPassword()));
            if(doctor.getImage() != null){
                preparedStatement.setString(8, ImageUtil.stringtoImage(doctor.getImage(),Constants.PROFILE_PIC_PREFIX+doctor.getRegistration_id(), Constants.FILE_FORMAT_JPG));
            }else {
                preparedStatement.setString(8, null);
            }
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error saving Doctor", ex);
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
            String SQL = "SELECT * FROM  `doctor` WHERE `REGISTRATION_NO` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String hash = resultSet.getString(DB_constants.Doctor.PASSWORD);
                return Encryptor.verifyPassword(password,hash);
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error doctor login", ex);
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

    public Doctor getDoctorDetails(String username){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `doctor` WHERE `REGISTRATION_NO` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            Doctor doctor = new Doctor();
            if (resultSet.next()){
                doctor.setRegistration_id(resultSet.getString("REGISTRATION_NO"));
                doctor.setSpeciality(resultSet.getString("SPECIALITY"));
                doctor.setName(resultSet.getString("NAME"));
                doctor.setGender(resultSet.getString("GENDER"));
                doctor.setEmail(resultSet.getString("EMAIL"));
                doctor.setMobile(resultSet.getString("CONTACT_NUMBER"));
                doctor.setImage(resultSet.getString("PROFILE_PICTURE"));
                return doctor;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting doctor details", ex);
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

    public ArrayList<Doctor> getAllDoctors(){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `doctor`";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Doctor> doctorsList = new ArrayList<>();
            while (resultSet.next()){
                Doctor doctor = new Doctor();
                doctor.setRegistration_id(resultSet.getString("REGISTRATION_NO"));
                doctor.setSpeciality(resultSet.getString("SPECIALITY"));
                doctor.setName(resultSet.getString("NAME"));
                doctor.setGender(resultSet.getString("GENDER"));
                doctor.setEmail(resultSet.getString("EMAIL"));
                doctor.setMobile(resultSet.getString("CONTACT_NUMBER"));
                doctor.setImage(resultSet.getString("PROFILE_PICTURE"));
                doctorsList.add(doctor);
            }
            return doctorsList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting doctors list", ex);
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

    public ArrayList<Patient> getPatiaentDetailsByDoctorID(String username){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM `patient` WHERE `NIC` IN (SELECT `PATIENT_NIC` FROM  `patient_has_doctor` WHERE `DOCTOR_REGISTRATION_NO` = ?)";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Patient> patientsList = new ArrayList<>();
            while (resultSet.next()){
                Patient patient = new Patient();
                patient.setNic(resultSet.getString("NIC"));
                patient.setName(resultSet.getString("PATIENT_NAME"));
                patient.setGender(resultSet.getString("GENDER"));
                patient.setEmail(resultSet.getString("EMAIL"));
                patient.setBirthday(resultSet.getString("BIRTHDAY"));
                patient.setMobile(resultSet.getString("CONTACT_NUMBER"));
                patient.setEmergency_contact(resultSet.getString("EMERGENCY_CONTACT_NUMBER"));
                patient.setImage(resultSet.getString("PROFILE_PICTURE"));
                patientsList.add(patient);
            }
            return patientsList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting doctor's patients details", ex);
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
