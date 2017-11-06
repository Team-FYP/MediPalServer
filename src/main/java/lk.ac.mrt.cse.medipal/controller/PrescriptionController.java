package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Prescription;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionController.class);

    public boolean savePrescription(Prescription prescription){
        boolean status = false;
        return status;
    }


    public ArrayList<Prescription> getPrescriptionsByPatient(String patientID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT `PRESCRIPTION_ID`, `DISEASE_DISEASE_ID`, `PATIENT_NIC`, `DOCTOR_ID` FROM  `prescription` WHERE `prescription`.`PATIENT_NIC` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Prescription> prescriptionList = new ArrayList<Prescription>();
            while (resultSet.next()){
                Prescription prescription = new Prescription();
                prescription.setPrescription_id(resultSet.getString("PRESCRIPTION_ID"));
                prescription.setDisease(resultSet.getString("DISEASE_DISEASE_ID"));
                prescription.setDoctor_id(resultSet.getString("DOCTOR_ID"));
                prescriptionList.add(prescription);
            }
            return prescriptionList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting prescription list by patient id", ex);
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
