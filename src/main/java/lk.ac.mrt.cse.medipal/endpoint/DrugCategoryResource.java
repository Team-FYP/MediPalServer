package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.ac.mrt.cse.medipal.controller.DrugCategoryController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * DrugCategory Resource endpoint
 */
@Path("/category")
public class DrugCategoryResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/categories")
    public Response allCategories() {
        Gson gson = new Gson();
        DrugCategoryController drugCategoryController = new DrugCategoryController();
        JsonObject returnObject = new JsonObject();
        ArrayList<DrugCategory> drugsCategoryList = drugCategoryController.getAllDrugsCategories();
        JsonArray drugsCategoryArray = gson.toJsonTree(drugsCategoryList).getAsJsonArray();
        returnObject.add("itemsList",drugsCategoryArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
