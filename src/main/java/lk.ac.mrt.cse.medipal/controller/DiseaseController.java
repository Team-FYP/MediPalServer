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
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DiseaseController.class);

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

    public String getDiseaseId(String diseaseName){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `disease` WHERE `DISEASE_NAME` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, diseaseName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("DISEASE_ID");
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

}
