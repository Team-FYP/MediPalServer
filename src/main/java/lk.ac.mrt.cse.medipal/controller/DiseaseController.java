package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Disease;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiseaseController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DoctorController.class);

    public ArrayList<Disease> getAllDiseases(){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `disease`";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Disease> diseasesList = new ArrayList<>();
            while (resultSet.next()){
                Disease disease = new Disease();
                disease.setDisease_id(resultSet.getString("DISEASE_ID"));
                disease.setDisease_name(resultSet.getString("DISEASE_NAME"));
                disease.setGraph_graph_id(resultSet.getString("GRAPH_GRAPH_ID"));
                diseasesList.add(disease);
            }
            return diseasesList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting diseases list", ex);
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

    public Disease getDiseaseDetails(int diseaseID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `disease` WHERE `DISEASE_ID` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, diseaseID);
            resultSet = preparedStatement.executeQuery();
            Disease disease = new Disease();
            if (resultSet.next()){
                disease.setDisease_id(resultSet.getString("DISEASE_ID"));
                disease.setDisease_name(resultSet.getString("DISEASE_NAME"));
                disease.setGraph_graph_id(resultSet.getString("GRAPH_GRAPH_ID"));
                return disease;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting disease details", ex);
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

    public boolean addDisease(Disease disease){
        boolean status = false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "INSERT INTO `disease` (`DISEASE_ID`,`DISEASE_NAME`,`GRAPH_GRAPH_ID`) VALUES (?, ?, ?) ";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, disease.getDisease_id());
            preparedStatement.setString(2, disease.getDisease_name());
            preparedStatement.setString(3, disease.getGraph_graph_id());
            resultSet = preparedStatement.executeQuery();
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error saving disease", ex);
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

    public boolean deleteDisease(String diseaseID){
        boolean status = false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "DELETE FROM disease WHERE DISEASE_ID = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, diseaseID);
            resultSet = preparedStatement.executeQuery();
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error deleting disease", ex);
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

    public boolean updateDisease(Disease disease){
        boolean status = false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "UPDATE disease SET DISEASE_NAME = ?, GRAPH_GRAPH_ID = ? WHERE DISEASE_ID = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, disease.getDisease_name());
            preparedStatement.setString(2, disease.getGraph_graph_id());
            preparedStatement.setString(3, disease.getDisease_id());
            resultSet = preparedStatement.executeQuery();
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error updating disease", ex);
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
