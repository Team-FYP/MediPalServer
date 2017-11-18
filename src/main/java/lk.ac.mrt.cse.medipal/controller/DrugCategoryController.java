package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DrugCategoryController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugCategoryController.class);


    public ArrayList<DrugCategory> getAllDrugsCategories(){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `category` ORDER BY ABS(`category`.`CATEGORY_ID`)";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<DrugCategory> drugCatagoryList = new ArrayList<DrugCategory>();
            ArrayList<DrugCategory> orderedDrugCatagoryList = new ArrayList<DrugCategory>();
            while (resultSet.next()){
                DrugCategory drugCategory = new DrugCategory();
                drugCategory.setCategory_id(String.valueOf(resultSet.getString("CATEGORY_ID")));
                drugCategory.setCategory_name(resultSet.getString("CATEGORY_NAME"));
                drugCategory.setGraph_id(resultSet.getString("GRAPH_GRAPH_ID"));
                drugCatagoryList.add(drugCategory);
            }

            return drugCatagoryList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drug category details", ex);
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

    public ArrayList<Drug> getDrugsByCategory(String categoryID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_id, drug.drug_name, drug.category_id, drug_disease.Disease FROM  drug INNER JOIN drug_disease ON drug_disease.Drug = drug.drug_id AND drug.category_id=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, categoryID);
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
            LOGGER.error("Error getting drugs of category details", ex);
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
