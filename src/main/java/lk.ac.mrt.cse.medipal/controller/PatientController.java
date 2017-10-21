package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Patient;
import org.apache.commons.dbutils.DbUtils;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lakshan on 9/19/17.
 */
public class PatientController {

    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    private static final Logger LOGGER = Logger.getLogger(PatientController.class.getName() );


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
            preparedStatement.setString(8, patient.getPassword());
            preparedStatement.setString(9, patient.getImage());

            status = 0 < preparedStatement.executeUpdate() ? true : false;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, ex.toString(), ex);
            }
        }
        return status;
    }
}
