/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ac.mrt.cse.medipal.Database;

import lk.ac.mrt.cse.medipal.constants.DB_constants;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ndine
 */
public class DB_Connection {
    private static DB_Connection dbConnection;
    private final BasicDataSource ds;
    private static final Logger LOGGER = Logger.getLogger(DB_Connection.class.getName());

    private DB_Connection()throws IOException, SQLException, PropertyVetoException {
        LOGGER.log(Level.ALL, "Connecting to database...");
        System.out.println("Connecting to database...");
        ds = new BasicDataSource();
        ds.setDriverClassName(DB_constants.DRIVER);
        ds.setUsername(DB_constants.USERNAME);
        ds.setPassword(DB_constants.PASSWORD);
        ds.setUrl(DB_constants.DB_URL+DB_constants.DB_NAME);
        // the settings below are optional -- dbcp can work with defaults
        //        ds.setMinIdle(5);
                ds.setMaxIdle(50);
                ds.setMaxTotal(100);
                ds.setMaxConnLifetimeMillis(10000);
        //        ds.setMaxOpenPreparedStatements(180);

    }
        
    public static synchronized DB_Connection getDBConnection() throws IOException, SQLException, PropertyVetoException {
        if (dbConnection == null) {
            dbConnection = new DB_Connection();
            return dbConnection;
        } else {
            return dbConnection;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}
