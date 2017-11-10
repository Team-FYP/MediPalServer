package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Disease;
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
            String SQL = "SELECT * FROM  `drug` WHERE `disease_id`= ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, diseaseID);
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
                if(categoryList.contains(resultSet.getString("CATEGORY_NAME")));
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
                if(drugList.contains(resultSet.getString("drug_name")));
                    drugList.add(resultSet.getString("CATEGORY_NAME"));
            }
            return drugList;
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
}
