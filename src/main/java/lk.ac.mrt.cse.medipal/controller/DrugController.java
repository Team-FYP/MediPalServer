package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Drug;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrugController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugController.class);

    public Drug getDrugDetails(String drugID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `drug` WHERE `drug_id` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, Integer.parseInt(drugID));
            resultSet = preparedStatement.executeQuery();
            Drug drug = new Drug();
            if (resultSet.next()){
                drug.setDrug_id(String.valueOf(resultSet.getInt("drug_id")));
                drug.setDrug_name(resultSet.getString("drug_name"));
                drug.setCategory_id(resultSet.getString("category_id"));
                return drug;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drug details", ex);
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

    public ArrayList<Drug> getAllDrugs(){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `drug`";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Drug> drugList = new ArrayList<Drug>();
            while (resultSet.next()){
                Drug drug = new Drug();
                drug.setDrug_id(String.valueOf(resultSet.getInt("drug_id")));
                drug.setDrug_name(resultSet.getString("drug_name"));
                drug.setCategory_id(resultSet.getString("category_id"));
                drugList.add(drug);
            }
            return drugList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drug details", ex);
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

    public ArrayList<Drug> getDrugsByDiease(String diseaseID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_id, drug.drug_name, drug.category_id, drug_disease.Disease FROM  drug INNER JOIN drug_disease ON drug_disease.Drug = drug.drug_id WHERE drug_disease.Disease=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, Integer.parseInt("2"));
            resultSet = preparedStatement.executeQuery();
            ArrayList<Drug> drugList = new ArrayList<Drug>();
            while (resultSet.next()){
                Drug drug = new Drug();
                drug.setDrug_id(String.valueOf(resultSet.getInt("drug_id")));
                drug.setDrug_name(resultSet.getString("drug_name"));
                drug.setCategory_id(resultSet.getString("category_id"));
                drug.setDisease_id(resultSet.getString("Disease"));
                drugList.add(drug);
            }
            return drugList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drugs for disease details", ex);
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

    public ArrayList<String> getDrugListByCategoryList(int diseaseID, ArrayList<String> categoryList){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_id, drug.drug_name, category.CATEGORY_NAME, drug_disease.Disease " +
                    "FROM drug INNER JOIN category ON drug.category_id=category.CATEGORY_ID " +
                    "INNER JOIN drug_disease ON drug_disease.Drug = drug.drug_id " +
                    "AND drug_disease.Disease= ?;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, diseaseID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<String> drugList = new ArrayList<String>();
            while (resultSet.next()){
                if(categoryList.contains(resultSet.getString("CATEGORY_NAME")))
                    drugList.add(resultSet.getString("drug_name"));
            }
            return drugList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drug list", ex);
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

    public ArrayList<String> getCategoryListByDrugList(int diseaseID, ArrayList<String> drugList){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_id, drug.drug_name, category.CATEGORY_NAME, drug_disease.Disease FROM drug " +
                    "INNER JOIN category ON drug.category_id=category.CATEGORY_ID " +
                    "INNER JOIN drug_disease ON drug_disease.Drug = drug.drug_id " +
                    "AND drug_disease.Disease= ?;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, diseaseID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<String> categoryList = new ArrayList<String>();
            while (resultSet.next()){
                if(drugList.contains(resultSet.getString("drug_name")))
                    categoryList.add(resultSet.getString("CATEGORY_NAME"));
            }
            return categoryList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting category list", ex);
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
    public ArrayList<String> getRecentDrugsByDisease(String patientID, String diseaseID){
        try {
            ArrayList<String> recentDrugList = new ArrayList<String>();
            PrescriptionController prescriptionController = new PrescriptionController();
            int lastPrescriptionId = prescriptionController.getLastPrescriptionIdByDisease(patientID, diseaseID);
            if(lastPrescriptionId == -1){
                return null;
            }
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_name FROM drug_prescription " +
                    "INNER JOIN drug ON drug_prescription.Drug_ID=drug.drug_id " +
                    "AND drug_prescription.Prescription_ID="+lastPrescriptionId;
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                recentDrugList.add(resultSet.getString("drug_name"));
            }
            return recentDrugList;
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
    public boolean addDrug(Drug drug){
        boolean status = false;
        PreparedStatement preparedStatementDisease;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String DRUGSQL = "INSERT INTO `drug` (`drug_id`,`drug_name`,`category_id`) VALUES (?, ?, ?) ";
            preparedStatement = connection.prepareStatement(DRUGSQL);
            preparedStatement.setInt(1, Integer.parseInt(drug.getDrug_id()));
            preparedStatement.setString(2, drug.getDrug_name());
            preparedStatement.setString(3, drug.getCategory_id());
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            int key = keys.getInt(1);
            preparedStatement.close();

            String DISEASESQL = "INSERT INTO `drug_disease` (`Drug`,`Disease`) VALUES (?, ?) ";
            preparedStatementDisease = connection.prepareStatement(DISEASESQL);
            preparedStatementDisease.setInt(1, key);
            preparedStatementDisease.setString(2, drug.getDisease_id());
            status = 0 < preparedStatementDisease.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error saving drug", ex);
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

    public String getDrugNameByID(int id){

        String drug_name = null;
        ResultSet resultSet1= null;
        try{
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug_name from drug where drug_id = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1,id);
            resultSet1 = preparedStatement.executeQuery();
            if(resultSet1.next()){
                drug_name = resultSet1.getString("drug_name");
            }

        }catch(SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting category list", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet1);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }

        return drug_name;

    }

    //Inserting the d-d interactions to the hash map
    public HashMap<String, Integer> getDrugToDrugConflictScore(){

        String id = null;
        String drug_name1 = null;
        String drug_name2 = null;
        HashMap<String, Integer> scoreValue = new HashMap<>();

        try{
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL_1 = "SELECT drug1_id, drug2_id, severity from d_d_interaction";
            preparedStatement = connection.prepareStatement(SQL_1);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                drug_name1 = getDrugNameByID(resultSet.getInt("drug1_id"));
                drug_name2 = getDrugNameByID(resultSet.getInt("drug2_id"));
                id = drug_name1 +"_"+ drug_name2;

                scoreValue.put(id, resultSet.getInt("severity"));

            }

        }catch(SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting category list", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }
//        System.out.println(scoreValue.get(0));
        /*for ( String key : scoreValue.keySet() ) {
            LOGGER.info(scoreValue.get(key));
        }*/
        return scoreValue;
    }

    public HashMap<String, Integer> getDrugToDiseaseConflictScore(){

        String id = null;
        String drug_name = null;
        String disease_name = null;
        HashMap<String, Integer> scoreValue = new HashMap<>();

        try{
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL_1 = "SELECT drug_id, disease_id, severity from d_di_interaction";
            preparedStatement = connection.prepareStatement(SQL_1);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                drug_name = getDrugNameByID(resultSet.getInt("drug_id"));

                if (resultSet.getInt("disease_id") == 1)
                    disease_name = "diabetes";
                else if (resultSet.getInt("disease_id") == 2)
                    disease_name = "hypertension";
                else if (resultSet.getInt("disease_id") == 3)
                    disease_name = "copd";

                id = drug_name +"_"+ disease_name;
                scoreValue.put(id, resultSet.getInt("severity"));
            }

        }catch(SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting category list", ex);
        } finally {
            try {
                DbUtils.closeQuietly(resultSet);
                DbUtils.closeQuietly(preparedStatement);
                DbUtils.close(connection);
            } catch (SQLException ex) {
                LOGGER.error("Error closing sql connection", ex);
            }
        }

        return scoreValue;
    }
}
