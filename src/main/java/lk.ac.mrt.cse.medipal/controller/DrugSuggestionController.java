package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.ConflictScoreValue;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugSuggestion;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DrugSuggestionController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DrugSuggestionController.class);
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Connection connection;

    public DrugSuggestion getSuggestedAndConflictedDrugs(ArrayList<String> suggestedDrugsNamesArray, ArrayList<ConflictScoreValue> conflictScoreValueArrayList){

        ArrayList<Drug> suggestedDrugArrayList = new ArrayList<>();
        DrugController drugController = new DrugController();
        for (String drugName: suggestedDrugsNamesArray
             ) {
            suggestedDrugArrayList.add(drugController.getDrugDetailsByName(drugName));
        }

        DrugSuggestion drugSuggestion = new DrugSuggestion();
        drugSuggestion.setConflicting_drugs(conflictScoreValueArrayList);
        drugSuggestion.setSuggested_drugs(suggestedDrugArrayList);
        return drugSuggestion;
    }
}
