package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DrugCategoryController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;

import javax.ws.rs.*;
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

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}/drugs")
    public Response allCategoryDrugs(@PathParam("id") String id) {
        Gson gson = new Gson();
        DrugCategoryController drugCategoryController = new DrugCategoryController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Drug> categoryDrugsList = drugCategoryController.getDrugsByCategory(id);
        JsonArray categoryDrugsArray = gson.toJsonTree(categoryDrugsList).getAsJsonArray();
        returnObject.add("itemsList",categoryDrugsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
