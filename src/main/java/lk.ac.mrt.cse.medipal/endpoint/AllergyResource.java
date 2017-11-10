package lk.ac.mrt.cse.medipal.endpoint;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.AllergyController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Allergy;
import lk.ac.mrt.cse.medipal.model.Drug;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Allergy Resource endpoint
 */
@Path("/allergy")
public class AllergyResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/addallergy")
    public Response addAllergy(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        AllergyController allergyController = new AllergyController();
        JsonObject returnObject = new JsonObject();
        Gson gson = new Gson();
        int allergy_id = jsonObject.get("allergy_id").getAsInt();
        String allergy_name = jsonObject.get("allergy_name").getAsString();
        Allergy allergy = new Allergy(allergy_id, allergy_name);
        boolean addAllergyStatus = allergyController.addAllergy(allergy);

        returnObject.addProperty("success", addAllergyStatus);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/allergies")
    public Response getAllergies() {
        Gson gson = new Gson();
        AllergyController allergyController = new AllergyController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Allergy> allergyList = allergyController.getAllAllergies();
        JsonArray allergiesArray = gson.toJsonTree(allergyList).getAsJsonArray();
        returnObject.add("allergiesList",allergiesArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}/")
    public Response getAllergy(@PathParam("id") int allergy_id) {
        Gson gson = new Gson();
        AllergyController allergyController = new AllergyController();
        JsonObject returnObject = new JsonObject();
        Allergy allergy = allergyController.getAllergy(allergy_id);
        String allergyDetails = gson.toJson(allergy);
        JsonObject allergyDetailObject = new JsonParser().parse(allergyDetails).getAsJsonObject();
        returnObject.add("allergyDetails",allergyDetailObject);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
