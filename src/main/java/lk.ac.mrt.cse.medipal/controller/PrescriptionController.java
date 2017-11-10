package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.constants.Constants;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.util.Encryptor;
import lk.ac.mrt.cse.medipal.util.ImageUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

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

    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }

    public boolean addPrescription(Prescription prescription){
        boolean status = false;
//        LocalDate localDate = LocalDate.now();
//        String date = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        PreparedStatement preparedStatementDrug;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQLPRESCRIPTION = "INSERT INTO `prescription` " +
                    " (`DATE`,`PATIENT_NIC`,`DISEASE_DISEASE_ID`,`DOCTOR_ID`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQLPRESCRIPTION);
            preparedStatement.setDate(1, today);
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

    public ArrayList<PrescriptionDrug> getCurrentPrescriptions(String patientID){
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_name, drug.category_id,drug_prescription.Drug_ID, drug_prescription.Prescription_ID, drug_prescription.Dosage, drug_prescription.Frequency, " +
                    "drug_prescription.Route, drug_prescription.Duration, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                    "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID " +
                    "AND prescription.PATIENT_NIC=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<PrescriptionDrug> prescriptionDrugsList = new ArrayList<>();
            while (resultSet.next()){
                java.sql.Date prescription_date = resultSet.getDate("DATE");
                long date_diff = daysBetween(today, prescription_date);
                if(date_diff <= resultSet.getInt("Duration")){
                    Drug drug = new Drug();
                    PrescriptionDrug prescriptionDrug = new PrescriptionDrug();
                    drug.setDrug_id(resultSet.getString("Drug_ID"));
                    drug.setDrug_name(resultSet.getString("drug_name"));
                    drug.setCategory_id(resultSet.getString("category_id"));
                    prescriptionDrug.setDrug(drug);
                    prescriptionDrug.setPrescriptionID(resultSet.getInt("Prescription_ID"));
                    prescriptionDrug.setDosage(resultSet.getString("Dosage"));
                    prescriptionDrug.setFrequency(resultSet.getString("Frequency"));
                    prescriptionDrug.setRoute(resultSet.getString("Route"));
                    prescriptionDrug.setDuration(resultSet.getInt("Duration"));
                    prescriptionDrugsList.add(prescriptionDrug);
                }
            }
            return prescriptionDrugsList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting patients current prescriptions list", ex);
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

    public PrescriptionDrug getLastPrescriptionForDisease(String patientID, int diseaseID){
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_name, drug.category_id,drug_prescription.Drug_ID, drug_prescription.Prescription_ID, drug_prescription.Dosage, drug_prescription.Frequency, " +
                    "drug_prescription.Route, drug_prescription.Duration, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                    "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID " +
                    "AND prescription.PATIENT_NIC=? AND prescription.DISEASE_DISEASE_ID=? ORDER BY drug_prescription.Prescription_ID";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            preparedStatement.setInt(2, diseaseID);
            resultSet = preparedStatement.executeQuery();
            PrescriptionDrug lastPrescription = new PrescriptionDrug();

            resultSet.last();
            Drug drug = new Drug();
            drug.setDrug_id(resultSet.getString("Drug_ID"));
            drug.setDrug_name(resultSet.getString("drug_name"));
            drug.setCategory_id(resultSet.getString("category_id"));
            lastPrescription.setDrug(drug);
            lastPrescription.setPrescriptionID(resultSet.getInt("Prescription_ID"));
            lastPrescription.setDosage(resultSet.getString("Dosage"));
            lastPrescription.setFrequency(resultSet.getString("Frequency"));
            lastPrescription.setRoute(resultSet.getString("Route"));
            lastPrescription.setDuration(resultSet.getInt("Duration"));
            return lastPrescription;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting patients last prescription for this disease", ex);
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
