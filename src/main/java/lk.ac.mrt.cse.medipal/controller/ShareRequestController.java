package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.ShareRequest;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class ShareRequestController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(ShareNotificationController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public boolean shareHistoryWithDoctor(ShareRequest shareRequest){
        boolean status = false;
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `patient_has_doctor` " +
                    " (`PATIENT_NIC`,`DOCTOR_REGISTRATION_NO`,`DATE`) " +
                    "VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, shareRequest.getPatient_id());
            preparedStatement.setString(2, shareRequest.getDoctor_id());
            preparedStatement.setDate(3, today);
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error sharing history with Doctor", ex);
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
