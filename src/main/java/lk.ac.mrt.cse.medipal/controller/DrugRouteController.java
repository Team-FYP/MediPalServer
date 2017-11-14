package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugRoute;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DrugRouteController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugRouteController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public ArrayList<DrugRoute> getAllDrugRoutes() {
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `drug_route`";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<DrugRoute> drugRouteList = new ArrayList<DrugRoute>();
            while (resultSet.next()){
                DrugRoute drugRoute = new DrugRoute();
                drugRoute.setRoute_id(resultSet.getInt("route_id"));
                drugRoute.setRoute_name(resultSet.getString("route_name"));
                drugRouteList.add(drugRoute);
            }
            return drugRouteList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drug routes details", ex);
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

    public DrugRoute getDrugRouteDetails(String drugRouteID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT * FROM  `drug_route` WHERE `route_id` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, Integer.parseInt(drugRouteID));
            resultSet = preparedStatement.executeQuery();
            DrugRoute drugRoute = new DrugRoute();
            if (resultSet.next()){
                drugRoute.setRoute_name(resultSet.getString("route_name"));
                return drugRoute;
            }
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting drug route details", ex);
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
