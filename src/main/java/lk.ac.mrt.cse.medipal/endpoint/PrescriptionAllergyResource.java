package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DiseaseController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionAllergyController;
import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.PrescriptionAllergy;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/allergy")
public class PrescriptionAllergyResource {
    public static Logger logger = Logger.getLogger(PatientResource.class);
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    public Response getPrescriptionAllergies(@PathParam("id") String patient_id) {
        Gson gson = new Gson();
        PrescriptionAllergyController prescriptionAllergyController = new PrescriptionAllergyController();
        JsonObject returnObject = new JsonObject();
        ArrayList<PrescriptionAllergy> prescriptionAllergiesArray= prescriptionAllergyController.getPrescriptionAllergiesByPatient(patient_id);
        JsonArray prescriptionAllergies = gson.toJsonTree(prescriptionAllergiesArray).getAsJsonArray();
        returnObject.add("allergyDetails", prescriptionAllergies);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
