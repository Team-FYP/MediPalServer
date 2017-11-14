package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.controller.DrugRouteController;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugRoute;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * DrugRoute Resource endpoint
 */
@Path("/drugroute")
public class DrugRouteResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/drugroutes")
    public Response allDrugRoutes() {
        Gson gson = new Gson();
        DrugRouteController drugRouteController = new DrugRouteController();
        JsonObject returnObject = new JsonObject();
        ArrayList<DrugRoute> drugRoutesList = drugRouteController.getAllDrugRoutes();
        JsonArray drugsRoutesArray = gson.toJsonTree(drugRoutesList).getAsJsonArray();
        returnObject.add("itemsList",drugsRoutesArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    public Response getDrugRoute(@PathParam("id") String drugRouteID) {
        Gson gson = new Gson();
        DrugRouteController drugRouteController = new DrugRouteController();
        JsonObject returnObject = new JsonObject();
        DrugRoute drugRouteDetail = drugRouteController.getDrugRouteDetails(drugRouteID);
        String drugRouteDetails = gson.toJson(drugRouteDetail);
        JsonObject drugRouteDetailObject = new JsonParser().parse(drugRouteDetails).getAsJsonObject();
        returnObject.add("drugRouteDetails", drugRouteDetailObject);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
