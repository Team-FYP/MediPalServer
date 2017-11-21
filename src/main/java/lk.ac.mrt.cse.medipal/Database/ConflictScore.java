package lk.ac.mrt.cse.medipal.Database;


import lk.ac.mrt.cse.medipal.controller.DrugController;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class ConflictScore {
    private static final DrugController drugController = new DrugController();
    private static ConflictScore conflictScore ;
    private static HashMap<String, Integer> drugToDrugConflictScore ;
    private static HashMap<String, Integer> drugToDiseaseConflictScore;
    private static final Logger LOGGER = Logger.getLogger(DB_Connection.class.getName());

    private ConflictScore()throws IOException, SQLException, PropertyVetoException {
        drugToDrugConflictScore = drugController.getDrugToDrugConflictScore();
        drugToDiseaseConflictScore = drugController.getDrugToDiseaseConflictScore();
    }

    public static synchronized ConflictScore getConflictScore() throws PropertyVetoException, SQLException, IOException {
        if (conflictScore == null) {
            conflictScore = new ConflictScore();
            return conflictScore;
        } else {
            return conflictScore;
        }
    }

    public HashMap<String, Integer> getDrugToDrugConflictScore() throws SQLException {
        return this.drugToDrugConflictScore;
    }

    public HashMap<String, Integer> getDrugToDiseaseConflictScore() throws SQLException {
        return this.drugToDiseaseConflictScore;
    }
}
