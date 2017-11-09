package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.DB_Connection;
import lk.ac.mrt.cse.medipal.model.PatientAllergy;
import lk.ac.mrt.cse.medipal.model.PrescriptionAllergy;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionAllergyController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public ArrayList<PrescriptionAllergy> getPrescriptionAllergiesByPatient(String patient_id){
        try {
            connection = DB_Connection.getDBConnection().getConnection();
            String SQL = "SELECT prescription_has_allergy.ALLERGY_ALLERGY_ID, prescription_has_allergy.PRESCRIPTION_PRESCRIPTION_ID, prescription_has_allergy.PRESCRIPTION_PATIENT_NIC, prescription_has_allergy.SEVERITY_NAME " +
                    "FROM  prescription_has_allergy " +
                    "INNER JOIN allergy ON allergy.ALLERGY_ID=prescription_has_allergy.ALLERGY_ALLERGY_ID AND prescription_has_allergy.PRESCRIPTION_PATIENT_NIC=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, patient_id);
            resultSet = preparedStatement.executeQuery();
            ArrayList<PrescriptionAllergy> patientAllergyList = new ArrayList<>();
            while (resultSet.next()){
                PrescriptionAllergy patient_allergy = new PrescriptionAllergy();
                patient_allergy.setAllergy_id(resultSet.getInt("ALLERGY_ALLERGY_ID"));
                patient_allergy.setPrescription_id(resultSet.getInt("PRESCRIPTION_PRESCRIPTION_ID"));
                patient_allergy.setSeverity(resultSet.getString("SEVERITY_NAME"));
                patientAllergyList.add(patient_allergy);
            }
            return patientAllergyList;
        } catch (SQLException | IOException | PropertyVetoException ex) {
            LOGGER.error("Error getting patient's prescription allergy details", ex);
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
