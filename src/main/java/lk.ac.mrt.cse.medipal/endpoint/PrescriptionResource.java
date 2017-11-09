package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionDrugController;
import lk.ac.mrt.cse.medipal.model.*;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/addprescription")
    public Response addPrescription(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        Patient patient = new Patient();
        int prescriptionID = 0;
        Doctor doctor = new Doctor();
        Drug drug1 = new Drug("1", "Metformin", "1","1");
        Drug drug2 = new Drug("4", "Glynase", "2    ","1");
        ArrayList<PrescriptionDrug> prescriptionDrugsArray = new ArrayList<>();
        PrescriptionDrug prescriptionDrug1 = new PrescriptionDrug();
        PrescriptionDrug prescriptionDrug2 = new PrescriptionDrug();
        patient.setNic(jsonObject.get("nic").getAsString());
        String disease_id = jsonObject.get("disease_id").getAsString();
        String doctor_id = jsonObject.get("doctor_id").getAsString();
        doctor.setRegistration_id(jsonObject.get("doctor_id").getAsString());
        JsonArray jsonArray = jsonObject.getAsJsonArray("drugs");

        prescriptionDrug1.setDrug(drug1);
        prescriptionDrug1.setDosage("3");
        prescriptionDrug1.setFrequency("3");
        prescriptionDrug1.setRoute("MOuth");
        prescriptionDrug1.setDuration(3);
        prescriptionDrugsArray.add(prescriptionDrug1);
        prescriptionDrug2.setDrug(drug2);
        prescriptionDrug2.setDosage("3");
        prescriptionDrug2.setFrequency("3");
        prescriptionDrug2.setRoute("MOuth");
        prescriptionDrug2.setDuration(3);
        prescriptionDrugsArray.add(prescriptionDrug2);
//        prescriptionDrug.setDrug(drug1);
//        prescriptionDrugsArray.add(prescriptionDrug);
//        if (jsonObject.get("drugs") != null) {
//            int len = jsonArray.size();
//            for (int i=0;i<len;i++){
//                prescriptionDrug.setDrug(jsonArray.get(i));
//            }
//        }

        Prescription prescription = new Prescription(prescriptionID,prescriptionDrugsArray,doctor,patient,disease_id,doctor_id, null);
        PrescriptionController prescriptionController = new PrescriptionController();
        JsonObject returnObject = new JsonObject();

        boolean addPrescription = prescriptionController.addPrescription(prescription);
        returnObject.addProperty("success",addPrescription);
        if(addPrescription){
//            Prescription savedPrescription = prescriptionController.get(nic);
//            String patientDetails = gson.toJson(savedPatient);
//            JsonObject patientDetailObject = new JsonParser().parse(patientDetails).getAsJsonObject();
//            returnObject.add("userData",patientDetailObject);
            returnObject.addProperty("message","Successfully Saved prescription");
        }
        else {
            returnObject.addProperty("message","Saving Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/patient/{id}/currentprescriptions")
    public Response getCurrentPrescriptions(@PathParam("id") String patientID) {
        Gson gson = new Gson();
        PrescriptionController prescriptionController = new PrescriptionController();
        JsonObject returnObject = new JsonObject();
        ArrayList<PrescriptionDrug> prescriptionsDrugsList = prescriptionController.getCurrentPrescriptions(patientID);
        JsonArray prescriptionsDrugsArray = gson.toJsonTree(prescriptionsDrugsList).getAsJsonArray();
        returnObject.add("currentPrescriptionsList",prescriptionsDrugsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }


}
