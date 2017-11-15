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
            String SQL = "SELECT * FROM  `category`";
            preparedStatement = connection.prepareStatement(SQL);
            resultSet = preparedStatement.executeQuery();
            ArrayList<DrugCategory> drugCatagoryList = new ArrayList<DrugCategory>();
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
}
