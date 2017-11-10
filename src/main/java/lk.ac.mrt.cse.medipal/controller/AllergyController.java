package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Allergy;
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

public class AllergyController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public boolean addAllergy(Allergy allergy){
        boolean status = false;
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "INSERT INTO `allergy` (`ALLERGY_NAME`) VALUES (?) ";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, allergy.getAllergy_name());
            status = 0 < preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error adding allergy", ex);
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

    public ArrayList<Allergy> getAllAllergies(){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `allergy`";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Allergy> allergyList = new ArrayList<Allergy>();
            while (resultSet.next()){
                Allergy allergy = new Allergy();
                allergy.setAllergy_id(resultSet.getInt("ALLERGY_ID"));
                allergy.setAllergy_name(resultSet.getString("ALLERGY_NAME"));
                allergyList.add(allergy);
            }
            return allergyList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting allergy list", ex);
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

    public Allergy getAllergy(int allergy_id){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `allergy` WHERE `ALLERGY_ID` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, allergy_id);
            resultSet = preparedStatement.executeQuery();
            Allergy allergy = new Allergy();
            if (resultSet.next()){
                allergy.setAllergy_id(resultSet.getInt("ALLERGY_ID"));
                allergy.setAllergy_name(resultSet.getString("ALLERGY_NAME"));
                return allergy;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting allergy by ID", ex);
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
