package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionDrugController {
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionDrugController.class);

    public ArrayList<PrescriptionDrug> getDrugsByPrescription(int prescriptionID){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT drug.drug_id, drug.drug_name, drug.category_id, drug_prescription.Dosage, drug_prescription.Frequency, drug_prescription.Route, drug_prescription.Duration, drug_prescription.Use_Time FROM  " +
                    "`drug` INNER JOIN drug_prescription ON drug.drug_id = drug_prescription.Drug_ID " +
                    "AND `drug_prescription`.`Prescription_ID` = ?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, prescriptionID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<PrescriptionDrug> prescriptionDrugsList = new ArrayList<>();
            while (resultSet.next()){
                Drug drug = new Drug();
                PrescriptionDrug prescriptionDrug = new PrescriptionDrug();
                drug.setDrug_id(resultSet.getString("drug_id"));
                drug.setDrug_name(resultSet.getString("drug_name"));
                drug.setCategory_id(resultSet.getString("category_id"));
                prescriptionDrug.setDrug(drug);
                prescriptionDrug.setDosage(resultSet.getString("Dosage"));
                prescriptionDrug.setFrequency(resultSet.getString("Frequency"));
                prescriptionDrug.setRoute(resultSet.getString("Route"));
                prescriptionDrug.setDuration(resultSet.getInt("Duration"));
                prescriptionDrug.setUseTime(resultSet.getString("Use_Time"));
                prescriptionDrugsList.add(prescriptionDrug);
            }
            return prescriptionDrugsList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting prescription's drugs list by prescription id", ex);
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
