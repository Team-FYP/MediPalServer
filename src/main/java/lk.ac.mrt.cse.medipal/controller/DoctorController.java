package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.constants.Constants;
import lk.ac.mrt.cse.medipal.constants.DB_constants;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.util.Encryptor;
import lk.ac.mrt.cse.medipal.util.ImageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            preparedStatement.setString(8, ImageUtil.stringtoImage(doctor.getImage(),Constants.PROFILE_PIC_PREFIX+doctor.getRegistration_id(), Constants.FILE_FORMAT_JPG));
            LOGGER.error("ajhdgsdbduk");
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
}
