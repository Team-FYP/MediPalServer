package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.model.Prescription;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Prescription Resource endpoint
 */
@Path("/prescription")
public class PrescriptionResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/patient/{id}")
    public Response getDrug(@PathParam("id") String patientID) {
        Gson gson = new Gson();
        PrescriptionController prescriptionController = new PrescriptionController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Prescription> prescriptionsList = prescriptionController.getPrescriptionsByPatient(patientID);
        JsonArray prescriptionsArray = gson.toJsonTree(prescriptionsList).getAsJsonArray();
        returnObject.add("prescriptionsList",prescriptionsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
