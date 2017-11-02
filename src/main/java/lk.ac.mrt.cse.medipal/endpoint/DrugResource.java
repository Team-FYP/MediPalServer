package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Drug;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @POST
    @Path("/drugs")
    public Response allDrugs(String request) {
        Gson gson = new Gson();
        DrugController drugController = new DrugController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Drug> drugsList = drugController.getAllDrugs();
        JsonArray drugsArray = gson.toJsonTree(drugsList).getAsJsonArray();
        returnObject.add("drugsList",drugsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
