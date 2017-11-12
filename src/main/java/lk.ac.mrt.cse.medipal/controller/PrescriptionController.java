package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
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
            String SQL = "SELECT `prescription`.`PRESCRIPTION_ID`, `prescription`.`DATE`, `prescription`.`DISEASE_DISEASE_ID`, `prescription`.`PATIENT_NIC`, `prescription`.`DOCTOR_ID`, `disease`.`DISEASE_NAME` FROM `prescription` INNER JOIN `disease` ON `prescription`.`DISEASE_DISEASE_ID`=`disease`.`DISEASE_ID` WHERE `prescription`.`PATIENT_NIC` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Prescription> prescriptionList = new ArrayList<Prescription>();
            while (resultSet.next()){
                Prescription prescription = new Prescription();
                prescription.setPrescription_id(resultSet.getInt("PRESCRIPTION_ID"));
                String prescriptionDate=resultSet.getDate("DATE").toString();
                prescription.setPrescription_date(prescriptionDate);
                prescription.setDisease(resultSet.getString("DISEASE_NAME"));
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

    public String getLastPrescriptionIdByDisease(String patientID, String diseaseID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT `PRESCRIPTION_ID` FROM  `prescription` ORDER BY `PRESCRIPTION_ID` DESC LIMIT 1 WHERE `prescription`.`PATIENT_NIC` = ? AND `prescription`.`DISEASE_DISEASE_ID` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            preparedStatement.setString(2, diseaseID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("DISEASE_DISEASE_ID");
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting last prescriptionId by disease", ex);
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
                    " (`DRUG_ID`, `PRESCRIPTION_ID`,`DOSAGE`,`FREQUENCY`,`ROUTE`,`DURATION`, `USE_TIME`, `Unit_Size`, `Start_Date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatementDrug = connection.prepareStatement(SQLDRUGS, Statement.RETURN_GENERATED_KEYS);
            for (PrescriptionDrug drug:prescription.getPrescription_drugs()
                    ) {
                preparedStatementDrug.setString(1, drug.getDrug().getDrug_id());
                preparedStatementDrug.setInt(2, key);
                preparedStatementDrug.setString(3, drug.getDosage());
                preparedStatementDrug.setString(4, drug.getFrequency());
                preparedStatementDrug.setString(5, drug.getRoute());
                preparedStatementDrug.setInt(6, drug.getDuration());
                preparedStatementDrug.setString(7, drug.getUseTime());
                preparedStatementDrug.setString(8, drug.getUnitSize());
                Date startDate = java.sql.Date.valueOf(drug.getStartDate());
                preparedStatementDrug.setDate(7, startDate);
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
                    "drug_prescription.Route, drug_prescription.Duration, drug_prescription.Use_Time, drug_prescription.Unit_Size, drug_prescription.Start_Date, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                    "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID " +
                    "AND prescription.PATIENT_NIC=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<PrescriptionDrug> prescriptionDrugsList = new ArrayList<>();
            while (resultSet.next()){
                java.sql.Date start_date = resultSet.getDate("Start_Date");
                long date_diff = daysBetween(today, start_date);
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
                    prescriptionDrug.setUseTime(resultSet.getString("Use_Time"));
                    prescriptionDrug.setUnitSize(resultSet.getString("Unit_size"));
                    String startDate=resultSet.getDate("Start_Date").toString();
                    prescriptionDrug.setStartDate(startDate);
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

    public Prescription getLastPrescriptionForDisease(String patientID, int diseaseID){
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        PreparedStatement preparedStatementPrescription;
        ResultSet resultSetPrescription;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "SELECT prescription.PRESCRIPTION_ID" +
                    "FROM prescription" +
                    "WHERE prescription.PATIENT_NIC=? AND prescription.DISEASE_DISEASE_ID=? ORDER BY drug_prescription.Prescription_ID";

            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, patientID);
            preparedStatement.setInt(2, diseaseID);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            ArrayList<PrescriptionDrug> lastPrescriptionDrugArray = new ArrayList<>();
            PrescriptionDrug lastPrescriptionDrug = new PrescriptionDrug();
            Prescription lastPrescription = new Prescription();
            int prescriptionID = resultSet.getInt("Prescription_ID");
            lastPrescriptionDrug.setPrescriptionID(prescriptionID);
            resultSet.close();

            String SQL = "SELECT drug.drug_name, drug.category_id,drug_prescription.Drug_ID, drug_prescription.Prescription_ID, drug_prescription.Dosage, drug_prescription.Frequency," +
                    "drug_prescription.Route, drug_prescription.Duration, drug_prescription.Use_Time, drug_prescription.Unit_Size, drug_prescription.Start_Date, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                    "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID " +
                    "AND prescription.PRESCRIPTION_ID=?";


            preparedStatementPrescription = connection.prepareStatement(SQL);
//            preparedStatementPrescription.setInt(1, prescriptionID);
            preparedStatementPrescription.setInt(1, 58);
            resultSetPrescription = preparedStatementPrescription.executeQuery();


            while(resultSetPrescription.next()){
                Drug drug = new Drug();
                drug.setDrug_id(resultSetPrescription.getString("Drug_ID"));
                drug.setDrug_name(resultSetPrescription.getString("drug_name"));
                drug.setCategory_id(resultSetPrescription.getString("category_id"));
                lastPrescriptionDrug.setDrug(drug);
                lastPrescriptionDrug.setPrescriptionID(resultSetPrescription.getInt("Prescription_ID"));
                lastPrescriptionDrug.setDosage(resultSetPrescription.getString("Dosage"));
                lastPrescriptionDrug.setFrequency(resultSetPrescription.getString("Frequency"));
                lastPrescriptionDrug.setRoute(resultSetPrescription.getString("Route"));
                lastPrescriptionDrug.setDuration(resultSetPrescription.getInt("Duration"));
                lastPrescriptionDrug.setUseTime(resultSetPrescription.getString("Use_Time"));
                lastPrescriptionDrug.setUnitSize(resultSetPrescription.getString("Unit_Size"));
                String startDate=resultSetPrescription.getDate("Start_Date").toString();
                lastPrescriptionDrug.setStartDate(startDate);
                lastPrescriptionDrugArray.add(lastPrescriptionDrug);
            }
            lastPrescription.setPrescription_drugs(lastPrescriptionDrugArray);
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
