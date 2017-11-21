package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.z3.Z3Exception;
import lk.ac.mrt.cse.medipal.controller.*;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

@Path("/prediction")
public class PredictionResource {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/levelupdruglist/{id}/{disease}")
    public Response getLevelUpDrugs(@PathParam("id") String patientID, @PathParam("disease") String disease) throws Z3Exception, DiseasePathController.TestFailedException {
        Gson gson = new Gson();
        JsonObject returnObject = new JsonObject();
        PredictionController predictionController = new PredictionController();
        String[] levelUpDrugs = predictionController.getLevelUpDrugs(patientID, disease);
        JsonArray drugList = gson.toJsonTree(Arrays.asList(levelUpDrugs)).getAsJsonArray();
        returnObject.add("drugList", drugList);
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
        String[] levelUpDrugs = predictionController.getLevelDownDrugs(patientID, disease);
        JsonArray drugList = gson.toJsonTree(Arrays.asList(levelUpDrugs)).getAsJsonArray();
        returnObject.add("drugList", drugList);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();

    }


    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/suggestedlist/{id}/{disease}")
    public Response getSuggestedAndConflictedDrugs(@PathParam("id") String patientID, @PathParam("disease") String disease) throws Z3Exception, DiseasePathController.TestFailedException {

        String[] prescribedDrugs = new String[]{"metformin", "Sitagliptin", "insulin"};
        String changedDrug = "metformin";

        Gson gson = new Gson();
        JsonObject returnObject = new JsonObject();
        PredictionController predictionController = new PredictionController();
        Object[] obj = predictionController.getSuggestedDrugs(patientID, disease, prescribedDrugs, changedDrug);
        JsonArray suggestedDrugs = gson.toJsonTree((ArrayList<String>)obj[1]).getAsJsonArray();
        JsonArray conflictedDrugs = gson.toJsonTree((ArrayList<String>)obj[0]).getAsJsonArray();
        returnObject.add("conflictedDrugs", conflictedDrugs);
        returnObject.add("suggestedDrugs", suggestedDrugs);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();

    }

}
