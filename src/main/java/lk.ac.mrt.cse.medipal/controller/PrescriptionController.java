package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.constants.Constants;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.util.Encryptor;
import lk.ac.mrt.cse.medipal.util.ImageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                prescription.setPrescription_id(resultSet.getInt("PRESCRIPTION_ID"));
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

    public boolean addPrescription(Prescription prescription){
        boolean status = false;
        LocalDate localDate = LocalDate.now();
        String date = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
        PreparedStatement preparedStatementDrug;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQLPRESCRIPTION = "INSERT INTO `prescription` " +
                    " (`DATE`,`PATIENT_NIC`,`DISEASE_DISEASE_ID`,`DOCTOR_ID`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQLPRESCRIPTION);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, prescription.getPatient().getNic());
            preparedStatement.setString(3, prescription.getDisease());
            preparedStatement.setString(4, prescription.getDoctor().getRegistration_id());
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            int key = keys.getInt(1);
            preparedStatement.close();
//
            String SQLDRUGS = "INSERT INTO `drug_prescription` " +
                    " (`DRUG_ID`, `PRESCRIPTION_ID`,`DOSAGE`,`FREQUENCY`,`ROUTE`,`DURATION`) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatementDrug = connection.prepareStatement(SQLDRUGS, Statement.RETURN_GENERATED_KEYS);
            for (PrescriptionDrug drug:prescription.getPrescription_drugs()
                 ) {
                preparedStatementDrug.setString(1, drug.getDrug().getDrug_id());
                preparedStatementDrug.setInt(2, key);
                preparedStatementDrug.setString(3, drug.getDosage());
                preparedStatementDrug.setString(4, drug.getFrequency());
                preparedStatementDrug.setString(5, drug.getRoute());
                preparedStatementDrug.setInt(6, drug.getDuration());
                preparedStatementDrug.executeUpdate();
            }

            status = true;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error adding Prescription", ex);
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
