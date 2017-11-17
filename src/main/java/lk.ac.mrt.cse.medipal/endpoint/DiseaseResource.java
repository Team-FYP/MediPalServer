package lk.ac.mrt.cse.medipal.endpoint;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DiseaseController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.Drug;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Disease Resource endpoint
 */
@Path("/disease")
public class DiseaseResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/diseases")
    public Response allDiseases() {
        Gson gson = new Gson();
        DiseaseController diseaseController = new DiseaseController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Disease> diseasesList = diseaseController.getAllDiseases();
        JsonArray diseasesArray = gson.toJsonTree(diseasesList).getAsJsonArray();
        returnObject.add("itemsList",diseasesArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    public Response getDrug(@PathParam("id") int diseaseID) {
        Gson gson = new Gson();
        DiseaseController diseaseController = new DiseaseController();
        JsonObject returnObject = new JsonObject();
        Disease diseaseDetail = diseaseController.getDiseaseDetails(diseaseID);
        String diseaseDetails = gson.toJson(diseaseDetail);
        JsonObject diseaseDetailObject = new JsonParser().parse(diseaseDetails).getAsJsonObject();
        returnObject.add("diseaseDetails", diseaseDetailObject);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}/drugs")
    public Response allDrugs(@PathParam("id") String id) {
        Gson gson = new Gson();
        DrugController drugController = new DrugController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Drug> drugsList = drugController.getDrugsByDiease(id);
        JsonArray drugsArray = gson.toJsonTree(drugsList).getAsJsonArray();
        returnObject.add("itemsList",drugsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
