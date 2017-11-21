package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.z3.Z3Exception;
import lk.ac.mrt.cse.medipal.controller.*;
import lk.ac.mrt.cse.medipal.model.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

@Path("/prediction")
public class PredictionResource {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PredictionResource.class);

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/levelupdruglist/{id}/{disease}")
    public Response getLevelUpDrugs(@PathParam("id") String patientID, @PathParam("disease") String disease) throws Z3Exception, DiseasePathController.TestFailedException {
        Gson gson = new Gson();
        JsonObject returnObject = new JsonObject();
        PredictionController predictionController = new PredictionController();
        DrugController drugController = new DrugController();
        String[] levelUpDrugs = predictionController.getLevelUpDrugs(patientID, disease);
        ArrayList<Drug> drugArrayList = new ArrayList<>();

        for (int i=1; i<levelUpDrugs.length; i++){
            drugArrayList.add(drugController.getDrugDetailsByName(levelUpDrugs[i]));
        }

        JsonArray drugsArray = gson.toJsonTree(drugArrayList).getAsJsonArray();
        returnObject.add("itemsList",drugsArray);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/leveldowndruglist/{id}/{disease}")
    public Response getLevelDownDrugs(@PathParam("id") String patientID, @PathParam("disease") String disease) throws Z3Exception, DiseasePathController.TestFailedException {
        Gson gson = new Gson();
        JsonObject returnObject = new JsonObject();
        PredictionController predictionController = new PredictionController();
        DrugController drugController = new DrugController();
        ArrayList<Drug> drugArrayList = new ArrayList<>();
        String[] levelDownDrugs = predictionController.getLevelDownDrugs(patientID, disease);
        for (int i=1; i<levelDownDrugs.length; i++){
            drugArrayList.add(drugController.getDrugDetailsByName(levelDownDrugs[i]));
        }

        JsonArray drugsArray = gson.toJsonTree(drugArrayList).getAsJsonArray();
        returnObject.add("itemsList",drugsArray);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();

    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/suggestedlist")
    public Response getSuggestedAndConflictedDrugs(String request) throws Z3Exception, DiseasePathController.TestFailedException {

        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        DrugChangeRequest drugChangeRequest = gson.fromJson(jsonObject.toString(), DrugChangeRequest.class);
        String patientID = drugChangeRequest.getPatient_id();
        String disease = drugChangeRequest.getDisease_name();
        ArrayList<Drug> drugArrayList = drugChangeRequest.getDrug_list();
        String changedDrug = drugChangeRequest.getNew_drug().getDrug_name();
        String[] prescribedDrugs = new String[drugArrayList.size()];
        for(int i=0; i<drugArrayList.size(); i++){
            prescribedDrugs[i] = drugArrayList.get(i).getDrug_name();
        }

        JsonObject returnObject = new JsonObject();
        PredictionController predictionController = new PredictionController();
        Object[] obj = predictionController.getSuggestedDrugs(patientID, disease, prescribedDrugs, changedDrug);
        ArrayList<String> suggestedDrugsNamesArray = (ArrayList<String>)obj[1];
        ArrayList<ConflictScoreValue> conflictedConflictScoreValues = (ArrayList<ConflictScoreValue>)obj[0];
        DrugSuggestionController drugSuggestionController = new DrugSuggestionController();
        DrugSuggestion drugSuggestion = drugSuggestionController.getSuggestedAndConflictedDrugs(suggestedDrugsNamesArray, conflictedConflictScoreValues);

        String drugSuggestionDetails = gson.toJson(drugSuggestion);
        returnObject = new JsonParser().parse(drugSuggestionDetails).getAsJsonObject();

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();

    }


}
