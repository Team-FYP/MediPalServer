package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.*;
import lk.ac.mrt.cse.medipal.util.MessageUtil;
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
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionController.class);
    private static Connection connection;

//    public boolean savePrescription(Prescription prescription){
//        boolean status = false;
//        return status;
//    }


    public ArrayList<Prescription> getPrescriptionsByPatient(String patientID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT `prescription`.`PRESCRIPTION_ID`, `prescription`.`DATE`, `prescription`.`DISEASE_DISEASE_ID`, `prescription`.`PATIENT_NIC`, `prescription`.`DOCTOR_ID`, `disease`.`DISEASE_NAME` " +
                    "FROM `prescription` " +
                    "INNER JOIN `disease` ON `prescription`.`DISEASE_DISEASE_ID`=`disease`.`DISEASE_ID` " +
                    "WHERE `prescription`.`PATIENT_NIC` = ? ORDER BY `prescription`.`DATE` DESC ";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Prescription> prescriptionList = new ArrayList<Prescription>();
            PrescriptionAllergyController prescriptionAllergy = new PrescriptionAllergyController();
            while (resultSet.next()){
                Prescription prescription = new Prescription();
                prescription.setPrescription_id(resultSet.getInt("PRESCRIPTION_ID"));
                String prescriptionDate=resultSet.getDate("DATE").toString();
                prescription.setPrescription_date(prescriptionDate);
                prescription.setDisease_id(resultSet.getString("DISEASE_DISEASE_ID"));
                prescription.setDoctor_id(resultSet.getString("DOCTOR_ID"));
                prescription.setDisease_name(resultSet.getString("DISEASE_NAME"));
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

    public int getLastPrescriptionIdByDisease(String patientID, String diseaseID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT `PRESCRIPTION_ID` FROM  `prescription` WHERE `prescription`.`PATIENT_NIC` = ? AND `prescription`.`DISEASE_DISEASE_ID` = ? ORDER BY `PRESCRIPTION_ID` DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            preparedStatement.setString(2, diseaseID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("PRESCRIPTION_ID");
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
        return -1;
    }

    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }

    public boolean addPrescription(Prescription prescription){
        boolean status = false;
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        PreparedStatement preparedStatementDrug = null;
        PreparedStatement preparedStatementDrugRoute = null;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            connection.setAutoCommit(false);
            String SQLPRESCRIPTION = "INSERT INTO `prescription` " +
                    " (`DATE`,`PATIENT_NIC`,`DISEASE_DISEASE_ID`,`DOCTOR_ID`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQLPRESCRIPTION);
            preparedStatement.setDate(1, today);
            preparedStatement.setString(2, prescription.getPatient().getNic());
            preparedStatement.setString(3, prescription.getDisease_id());
            preparedStatement.setString(4, prescription.getDoctor().getRegistration_id());
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            int key = keys.getInt(1);
            preparedStatement.close();
//

            String SQLROUTES = "SELECT drug_route.route_id FROM drug_route WHERE drug_route.route_name= ?";
            preparedStatementDrug = connection.prepareStatement(SQLROUTES);
            for (PrescriptionDrug drug:prescription.getPrescription_drugs()
                    ) {
                preparedStatementDrug.setString(1, drug.getRoute());
                resultSet = preparedStatementDrug.executeQuery();
                drug.setRoute("1");
//                drug.setRoute(String.valueOf(resultSet.getInt("route_id")));
                resultSet.close();
            }

            String SQLDRUGS = "INSERT INTO `drug_prescription` " +
                    " (`DRUG_ID`, `PRESCRIPTION_ID`,`DOSAGE`,`FREQUENCY`,`ROUTE`,`DURATION`, `USE_TIME`, `Unit_Size`, `Start_Date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatementDrug = connection.prepareStatement(SQLDRUGS, Statement.RETURN_GENERATED_KEYS);
            for (PrescriptionDrug drug:prescription.getPrescription_drugs()
                    ) {
//                LOGGER.info(drug.getDrug().getDrug_id());
                preparedStatementDrug.setString(1, drug.getDrug().getDrug_id());
                preparedStatementDrug.setInt(2, key);
                preparedStatementDrug.setString(3, drug.getDosage());
                preparedStatementDrug.setString(4, drug.getFrequency());
                preparedStatementDrug.setInt(5, Integer.parseInt(drug.getRoute()));
                preparedStatementDrug.setInt(6, drug.getDuration());
                preparedStatementDrug.setString(7, drug.getUseTime());
                preparedStatementDrug.setString(8, drug.getUnitSize());
                Date startDate = java.sql.Date.valueOf(drug.getStartDate());
                preparedStatementDrug.setDate(9, startDate);
                preparedStatementDrug.executeUpdate();
            }
            DbUtils.commitAndCloseQuietly(connection);

            status = true;
        } catch (SQLException | IOException | PropertyVetoException ex) {

            try {
                DbUtils.rollback(connection) ;
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Error rollback", ex);
            }
            LOGGER.error("Error adding Prescription", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.closeQuietly(preparedStatementDrug);
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
                    "drug_route.route_name, drug_prescription.Duration, drug_prescription.Use_Time, drug_prescription.Unit_Size, drug_prescription.Start_Date, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                    "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID INNER JOIN drug_route ON drug_route.route_id=drug_prescription.Route " +
                    "AND prescription.PATIENT_NIC=?  ORDER BY `drug_prescription`.`Prescription_ID` DESC";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patientID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<PrescriptionDrug> prescriptionDrugsList = new ArrayList<>();
            while (resultSet.next()){
                java.sql.Date start_date = resultSet.getDate("Start_Date");
                int drug_id = resultSet.getInt("Drug_ID");
                ArrayList<Integer> drugIDList = new ArrayList<>();
                if(!drugIDList.contains(drug_id)){
                    long date_diff = daysBetween(today, start_date);
                    if(date_diff <= resultSet.getInt("Duration")){
                        Drug drug = new Drug();
                        PrescriptionDrug prescriptionDrug = new PrescriptionDrug();
                        drug.setDrug_id(String.valueOf(drug_id));
                        drug.setDrug_name(resultSet.getString("drug_name"));
                        drug.setCategory_id(resultSet.getString("category_id"));
                        prescriptionDrug.setDrug(drug);
                        prescriptionDrug.setPrescriptionID(resultSet.getInt("Prescription_ID"));
                        prescriptionDrug.setDosage(resultSet.getString("Dosage"));
                        prescriptionDrug.setFrequency(resultSet.getString("Frequency"));
                        prescriptionDrug.setRoute(resultSet.getString("route_name"));
                        prescriptionDrug.setDuration(resultSet.getInt("Duration"));
                        prescriptionDrug.setUseTime(resultSet.getString("Use_Time"));
                        prescriptionDrug.setUnitSize(resultSet.getString("Unit_size"));
                        String startDate=resultSet.getDate("Start_Date").toString();
                        prescriptionDrug.setStartDate(startDate);
                        prescriptionDrugsList.add(prescriptionDrug);
                    }
                    drugIDList.add(drug_id);
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
        PreparedStatement preparedStatementPrescription = null;
        ResultSet resultSetPrescription = null;
        ArrayList<PrescriptionDrug> lastPrescriptionDrugArray = new ArrayList<>();
        Prescription lastPrescription = new Prescription();
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "SELECT prescription.PRESCRIPTION_ID" +
                    " FROM prescription" +
                    " WHERE prescription.PATIENT_NIC=? AND prescription.DISEASE_DISEASE_ID=? ORDER BY prescription.PRESCRIPTION_ID DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, patientID);
            preparedStatement.setInt(2, diseaseID);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int prescriptionID = resultSet.getInt("Prescription_ID");
                lastPrescription.setPrescription_id(prescriptionID);

                String SQL = "SELECT drug.drug_name, drug.category_id,drug_prescription.Drug_ID, drug_prescription.Prescription_ID, drug_prescription.Dosage, drug_prescription.Frequency," +
                        "drug_route.route_name, drug_prescription.Duration, drug_prescription.Use_Time, drug_prescription.Unit_Size, drug_prescription.Start_Date, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                        "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID INNER JOIN drug_route ON drug_route.route_id=drug_prescription.Route " +
                        "AND prescription.PRESCRIPTION_ID=?";

                preparedStatementPrescription = connection.prepareStatement(SQL);
                preparedStatementPrescription.setInt(1, prescriptionID);
                resultSetPrescription = preparedStatementPrescription.executeQuery();

                while(resultSetPrescription.next()){
                    Drug drug = new Drug();
                    PrescriptionDrug lastPrescriptionDrug = new PrescriptionDrug();
                    drug.setDrug_id(resultSetPrescription.getString("Drug_ID"));
                    drug.setDrug_name(resultSetPrescription.getString("drug_name"));
                    drug.setCategory_id(resultSetPrescription.getString("category_id"));
                    lastPrescriptionDrug.setDrug(drug);
                    lastPrescriptionDrug.setPrescriptionID(resultSetPrescription.getInt("Prescription_ID"));
                    lastPrescriptionDrug.setDosage(resultSetPrescription.getString("Dosage"));
                    lastPrescriptionDrug.setFrequency(resultSetPrescription.getString("Frequency"));
                    lastPrescriptionDrug.setRoute(resultSetPrescription.getString("route_name"));
                    lastPrescriptionDrug.setDuration(resultSetPrescription.getInt("Duration"));
                    lastPrescriptionDrug.setUseTime(resultSetPrescription.getString("Use_Time"));
                    lastPrescriptionDrug.setUnitSize(resultSetPrescription.getString("Unit_Size"));
                    String startDate=resultSetPrescription.getDate("Start_Date").toString();
                    lastPrescriptionDrug.setStartDate(startDate);
                    lastPrescriptionDrugArray.add(lastPrescriptionDrug);
                    lastPrescription.setDisease_id(resultSetPrescription.getString("DISEASE_DISEASE_ID"));
                }
                lastPrescription.setPrescription_drugs(lastPrescriptionDrugArray);
                return lastPrescription;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting patients last prescription for this disease", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(resultSetPrescription);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.closeQuietly(preparedStatementPrescription);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return null;
    }

    public boolean addPrescriptionNotification(Prescription prescription){
        boolean status=false;
        DoctorController doctorController = new DoctorController();
        String doctor_name = doctorController.getDoctorNameByID(prescription.getDoctor_id());
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        try {

            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "INSERT INTO `prescription_notification` " +
                    " (`patient_id`,`doctor_id`, `prescription_id`, `message`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setString(1, prescription.getPatient().getNic());
            preparedStatement.setString(2, prescription.getDoctor_id());
            preparedStatement.setInt(3, prescription.getPrescription_id());
            preparedStatement.setString(4, MessageUtil.PrescriptionAddedMessageBuild(doctor_name));
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error adding prescription notification", ex);
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

    public int getLastInsertedPrescriptionID(){
        int prescription_id = 0;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL1 = "SELECT PRESCRIPTION_ID FROM `prescription` ORDER BY PRESCRIPTION_ID DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(SQL1);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                prescription_id = resultSet.getInt("PRESCRIPTION_ID");
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting last prescription id", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
        return prescription_id;
    }

    public Prescription getPrescriptionByID(int prescription_id){
        PreparedStatement preparedStatementPrescription = null;
        ResultSet resultSetPrescription;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            ArrayList<PrescriptionDrug> lastPrescriptionDrugArray = new ArrayList<>();
            Prescription prescription = new Prescription();
            DiseaseController diseaseController = new DiseaseController();

            prescription.setPrescription_id(prescription_id);

            String SQL = "SELECT drug.drug_name, drug.category_id,drug_prescription.Drug_ID, drug_prescription.Prescription_ID, drug_prescription.Dosage, drug_prescription.Frequency," +
                    "drug_route.route_name, drug_prescription.Duration, drug_prescription.Use_Time, drug_prescription.Unit_Size, drug_prescription.Start_Date, prescription.DATE, prescription.DISEASE_DISEASE_ID, prescription.DOCTOR_ID " +
                    "FROM drug INNER JOIN drug_prescription ON drug.drug_id=drug_prescription.Drug_ID INNER JOIN prescription ON drug_prescription.Prescription_ID=prescription.PRESCRIPTION_ID INNER JOIN drug_route ON drug_route.route_id=drug_prescription.Route " +
                    "AND prescription.PRESCRIPTION_ID=?";

            preparedStatementPrescription = connection.prepareStatement(SQL);
            preparedStatementPrescription.setInt(1, prescription_id);
            resultSetPrescription = preparedStatementPrescription.executeQuery();

            while(resultSetPrescription.next()){
                Drug drug = new Drug();
                PrescriptionDrug lastPrescriptionDrug = new PrescriptionDrug();
                drug.setDrug_id(resultSetPrescription.getString("Drug_ID"));
                drug.setDrug_name(resultSetPrescription.getString("drug_name"));
                drug.setCategory_id(resultSetPrescription.getString("category_id"));
                lastPrescriptionDrug.setDrug(drug);
                lastPrescriptionDrug.setPrescriptionID(resultSetPrescription.getInt("Prescription_ID"));
                lastPrescriptionDrug.setDosage(resultSetPrescription.getString("Dosage"));
                lastPrescriptionDrug.setFrequency(resultSetPrescription.getString("Frequency"));
                lastPrescriptionDrug.setRoute(resultSetPrescription.getString("route_name"));
                lastPrescriptionDrug.setDuration(resultSetPrescription.getInt("Duration"));
                lastPrescriptionDrug.setUseTime(resultSetPrescription.getString("Use_Time"));
                lastPrescriptionDrug.setUnitSize(resultSetPrescription.getString("Unit_Size"));
                String startDate=resultSetPrescription.getDate("Start_Date").toString();
                lastPrescriptionDrug.setStartDate(startDate);
                lastPrescriptionDrugArray.add(lastPrescriptionDrug);
                prescription.setDisease_id(String.valueOf(resultSetPrescription.getInt("DISEASE_DISEASE_ID")));
                prescription.setDisease_name(diseaseController.getDiseaseDetails(resultSetPrescription.getInt("DISEASE_DISEASE_ID")).getDisease_name());
            }
            prescription.setPrescription_drugs(lastPrescriptionDrugArray);
            preparedStatementPrescription.close();


            String SQL1 = "SELECT * FROM prescription_has_allergy WHERE PRESCRIPTION_PRESCRIPTION_ID=?";
            preparedStatement = connection.prepareStatement(SQL1);
            preparedStatement.setInt(1, prescription_id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                PrescriptionAllergy prescriptionAllergy = new PrescriptionAllergy();
                prescriptionAllergy.setPrescription_allergy_id(resultSet.getInt("PRESCRIPTION_PRESCRIPTION_ID"));
                prescriptionAllergy.setAllergy_description(resultSet.getString("ALLERGY_DESCRIPTION"));
                prescriptionAllergy.setSeverity(resultSet.getString("SEVERITY_NAME"));
                prescriptionAllergy.setPatient_id(resultSet.getString("PRESCRIPTION_PATIENT_NIC"));
                prescription.setPrescriptionAllergy(prescriptionAllergy);
            }

            return prescription;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting prescription for by prescriptionID", ex);
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

    public Prescription getPrescriptionByDrugsList(ArrayList<Drug> drugs_list){
        ArrayList<PrescriptionDrug> prescriptionDrugsList = new ArrayList<>();
        Prescription prescription = new Prescription();
        for (Drug drug: drugs_list
                ) {
            PrescriptionDrug prescriptionDrug = new PrescriptionDrug();
            prescriptionDrug.setDrug(drug);
            prescriptionDrugsList.add(prescriptionDrug);
        }
        prescription.setPrescription_drugs(prescriptionDrugsList);
        return prescription;
    }

//    public ArrayList<String> getDrugsNameListByPrescriptionDrugsList(ArrayList<PrescriptionDrug> prescriptionDrugArrayList){
//        try {
//            connection = DB_Connection.getDBConnection().getConnection();
//            String SQL = "SELECT `drug_name` FROM  `drug` WHERE `drug_id` = ?";
//            preparedStatement = connection.prepareStatement(SQL);
//            preparedStatement.setInt(1, prescription.getPrescription_drugs().);
//            preparedStatement.setString(2, diseaseID);
//            resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()){
//                return resultSet.getInt("PRESCRIPTION_ID");
//            }
//        } catch (SQLException | IOException | PropertyVetoException ex) {
//            LOGGER.error("Error getting last prescriptionId by disease", ex);
//        } finally {
//            try {
//                DbUtils.closeQuietly(resultSet);
//                DbUtils.closeQuietly(preparedStatement);
//                DbUtils.close(connection);
//            } catch (SQLException ex) {
//                LOGGER.error("Error closing sql connection", ex);
//            }
//        }
//        return -1;
//    }
}
