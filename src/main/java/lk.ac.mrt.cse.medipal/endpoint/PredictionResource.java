package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.microsoft.z3.Z3Exception;
import lk.ac.mrt.cse.medipal.controller.DiseaseController;
import lk.ac.mrt.cse.medipal.controller.DiseasePathController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.controller.ScoreCalculationController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/prediction")
public class PredictionResource {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/druglist/{id}")
    public Response getSuitableDrugs(@PathParam("id") String patientID, String disease) throws Z3Exception, DiseasePathController.TestFailedException {
        Gson gson = new Gson();
        DiseaseController diseaseController = new DiseaseController();
        DrugController drugController = new DrugController();
        DiseasePathController pathController = new DiseasePathController();
        ScoreCalculationController calculationController = new ScoreCalculationController();
        String diseaseId = diseaseController.getDiseaseId(disease);
        ArrayList<String> currentDrugs = drugController.getRecentDrugsByDisease(patientID, diseaseId);
        ArrayList<String> patientHistory = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ArrayList<String> list = drugController.getRecentDrugsByDisease(patientID, Integer.toString(i));
            if(list != null)
                if(i == 1)
                    list.add("diabetes");
                else if(i == 2){
                    list.add("hypertension");
                }else if(i == 3){
                    list.add("copd");
                }
                patientHistory.addAll(list);
        }

//        ArrayList<String> categoryList = drugController.getCategoryListByDrugList(diseaseId, currentDrugs);
//        categoryList.add(disease);
//        ArrayList<String> selectedCategoryList = pathController.findDiseaseMeds(disease, categoryList.toArray(new String[categoryList.size()]));
//        ArrayList<String> selectedDrugList = drugController.getDrugListByCategoryList(diseaseId, selectedCategoryList);
//        selectedDrugList.add(disease);
//        Object[] matrices = calculationController.buidArrays(selectedDrugList.toArray(new String[selectedDrugList.size()]), patientHistory.toArray(new String[patientHistory.size()]));

        return null;
    }

}
