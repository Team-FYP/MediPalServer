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

public class DrugController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugController.class);

    public Drug getDrugDetails(int drugID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `drug` WHERE `drug_id` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, drugID);
            resultSet = preparedStatement.executeQuery();
            Drug drug = new Drug();
            if (resultSet.next()){
                drug.setDrug_id(resultSet.getInt("drug_id"));
                drug.setDrug_name(resultSet.getString("drug_name"));
                drug.setCategory_id(resultSet.getInt("category_id"));
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
                drug.setDrug_id(resultSet.getInt("drug_id"));
                drug.setDrug_name(resultSet.getString("drug_name"));
                drug.setCategory_id(resultSet.getInt("category_id"));
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
}
