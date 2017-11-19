package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.*;
import lk.ac.mrt.cse.medipal.model.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PrescriptionResource.class);

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/patient/{id}")
    public Response getPrescriptionByPatient(@PathParam("id") String patientID) {
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
        returnObject.add("itemsList",prescriptionsArray);
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
        DrugController drugController = new DrugController();
        Patient patient = new Patient();
        ArrayList<PrescriptionDrug> prescriptionDrugsArray = new ArrayList<>();
        Gson gson = new Gson();
        int prescriptionID = 0;
        Doctor doctor = new Doctor();
        patient = gson.fromJson(jsonObject.get("patient").getAsJsonObject().toString(), Patient.class);
        Prescription prescription = gson.fromJson(jsonObject.toString(), Prescription.class);
        PrescriptionController prescriptionController = new PrescriptionController();
        JsonObject returnObject = new JsonObject();
        boolean addPrescription = prescriptionController.addPrescription(prescription);
        prescriptionID = prescriptionController.getLastInsertedPrescriptionID();
        prescription.setPrescription_id(prescriptionID);
        returnObject.addProperty("success",addPrescription);
        if(addPrescription){
            boolean addPrescriptionNotification = prescriptionController.addPrescriptionNotification(prescription);
            if(addPrescriptionNotification){
                returnObject.addProperty("message","Successfully Saved prescription");
            }
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
        returnObject.add("itemsList",prescriptionsDrugsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/patient/{id}/lastprescription/{disease}")
    public Response getLastPrescriptionsForDisease(@PathParam("id") String patientID, @PathParam("disease") int diseaseID) {
        Gson gson = new Gson();
        PrescriptionController prescriptionController = new PrescriptionController();
        JsonObject returnObject = new JsonObject();

        int prescriptionID = prescriptionController.getLastPrescriptionIdByDisease(patientID, String.valueOf(diseaseID));
        if(prescriptionID<0){
            returnObject.addProperty("itemsList",prescriptionID);
        }else {
            Prescription lastPrescriptions = prescriptionController.getLastPrescriptionForDisease(patientID, diseaseID);
            String lastPrescriptionsDetails = gson.toJson(lastPrescriptions);
            returnObject = new JsonParser().parse(lastPrescriptionsDetails).getAsJsonObject();
        }

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();


    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/checkprescription")
    public Response checkPrescription(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        DrugController drugController = new DrugController();
        Patient patient = new Patient();
        ArrayList<PrescriptionDrug> prescriptionDrugsArray = new ArrayList<>();
        Gson gson = new Gson();
        int prescriptionID = 0;
        Doctor doctor = new Doctor();
        patient.setNic(jsonObject.get("nic").getAsString());
        String disease_id = jsonObject.get("disease_id").getAsString();

        JSONObject drugsArrayObject = new JSONObject(jsonObject.toString());
        JSONArray drugsArray = drugsArrayObject.getJSONArray("prescription_drugs");

        for(int i=0; i<drugsArray.length(); i++){
            PrescriptionDrug drug = new PrescriptionDrug();
            JSONObject drugObject = drugsArray.getJSONObject(i);
            drug.setDrug(drugController.getDrugDetails(drugObject.get("drug_id").toString()));
            prescriptionDrugsArray.add(drug);
        }

        Prescription prescription = new Prescription(prescriptionID,prescriptionDrugsArray,doctor,patient,disease_id,null, null);
        PrescriptionController prescriptionController = new PrescriptionController();
        JsonObject returnObject = new JsonObject();
        boolean addPrescription = prescriptionController.addPrescription(prescription);
        returnObject.addProperty("success",addPrescription);
        if(addPrescription){
            returnObject.addProperty("message","Successfully Saved prescription");
        }
        else {
            returnObject.addProperty("message","Saving Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

}
