package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.*;
import lk.ac.mrt.cse.medipal.model.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/prescription/allergy")
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
        returnObject.add("prescriptionAllergyDetails", prescriptionAllergies);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/addallergy")
    public Response addPrescriptionAllergy(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        PrescriptionAllergy prescriptionAllergy = gson.fromJson(jsonObject.toString(), PrescriptionAllergy.class);
        PrescriptionAllergyController prescriptionAllergyController = new PrescriptionAllergyController();
        AllergyNotificationController allergyNotificationController = new AllergyNotificationController();
        JsonObject returnObject = new JsonObject();
        boolean addPrescriptionAllergy = prescriptionAllergyController.addPrescriptionAllergy(prescriptionAllergy);
        returnObject.addProperty("success",addPrescriptionAllergy);
        int prescriptionAllergyID = prescriptionAllergyController.getLastInsertedPrescriptionAllergyID();
        prescriptionAllergy.setPrescription_allergy_id(prescriptionAllergyID);
        if(addPrescriptionAllergy){
            boolean addAllergyNotification = allergyNotificationController.addPrescriptionAllergyNotification(prescriptionAllergy);
            if(addAllergyNotification){
                returnObject.addProperty("message","Successfully Saved Allergy for Prescription");
            }
        }
        else {
            returnObject.addProperty("message","Saving Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
