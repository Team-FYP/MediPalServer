package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Drug;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Drug Resource endpoint
 */
@Path("/drug")
public class DrugResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/drugs")
    public Response allDrugs() {
        Gson gson = new Gson();
        DrugController drugController = new DrugController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Drug> drugsList = drugController.getAllDrugs();
        JsonArray drugsArray = gson.toJsonTree(drugsList).getAsJsonArray();
        returnObject.add("drugsList",drugsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    public Response getDrug(@PathParam("id") String drugID) {
        Gson gson = new Gson();
        DrugController drugController = new DrugController();
        JsonObject returnObject = new JsonObject();
        Drug drugDetail = drugController.getDrugDetails(drugID);
        String drugDetails = gson.toJson(drugDetail);
        JsonObject drugDetailObject = new JsonParser().parse(drugDetails).getAsJsonObject();
        returnObject.add("drugDetails", drugDetailObject);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/adddrug")
    public Response addDrug(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        String drug_id = jsonObject.get("drug_id").getAsString();
        String drug_name = jsonObject.get("drug_name").getAsString();
        String category = jsonObject.get("category").getAsString();
        String disease_id = jsonObject.get("disease_id").getAsString();
        DrugController drugController = new DrugController();
        JsonObject returnObject = new JsonObject();
        Drug drug = new Drug(drug_id, drug_name,category, disease_id);
        boolean drugDetail = drugController.addDrug(drug);
        returnObject.addProperty("drugDetails", drugDetail);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @GET
//    @Path("/testhashmap")
//    public Response getDrug() {
//        Gson gson = new Gson();
//        DrugController drugController = new DrugController();
//        ArrayList<String> categoryList = new ArrayList<>();
//        categoryList.add("metformin");
//        categoryList.add("sulfonylurea");
//        JsonObject returnObject = new JsonObject();
//
//        ArrayList<String> categoryListNew = drugController.getDrugListByCategoryList(1, categoryList);
//        JsonArray drugsArray = gson.toJsonTree(categoryListNew).getAsJsonArray();
//        returnObject.add("drugsList",drugsArray);
//        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
//    }
}
