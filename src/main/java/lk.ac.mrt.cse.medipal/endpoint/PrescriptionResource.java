package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionDrugController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import org.json.JSONObject;

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
        PatientController patientController = new PatientController();
        DoctorController doctorController =  new DoctorController();
        PrescriptionDrugController prescriptionDrugController =  new PrescriptionDrugController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Prescription> prescriptionsList = prescriptionController.getPrescriptionsByPatient(patientID);
        for (Prescription prescription:prescriptionsList) {
            Doctor doctor = doctorController.getDoctorDetails(prescription.getDoctor_id());
            ArrayList<PrescriptionDrug> prescriptionDrugsArray = prescriptionDrugController.getDrugsByPrescription(prescription.getPrescription_id());
            prescription.setDoctor(doctor);
            prescription.setPrescription_drugs(prescriptionDrugsArray);
        }
        JsonArray prescriptionsArray = gson.toJsonTree(prescriptionsList).getAsJsonArray();
        returnObject.add("prescriptionsList",prescriptionsArray);
        Patient patient = patientController.getPatiaentDetails(patientID);
        String patientDetails = gson.toJson(patient);
        JsonObject patientDetailObject = new JsonParser().parse(patientDetails).getAsJsonObject();
        returnObject.add("patient",patientDetailObject);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
