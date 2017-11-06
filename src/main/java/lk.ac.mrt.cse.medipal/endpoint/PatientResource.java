package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.model.Patient;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lakshan on 9/19/17.
 */
@Path("/patient")
public class PatientResource {
    public static Logger logger = Logger.getLogger(PatientResource.class);
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/login")
    public Response login(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        PatientController patientController = new PatientController();
        boolean result = patientController.checkLogin(username,password);
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("success",result);
        if (result) {
            Patient patient = patientController.getPatiaentDetails(username);
            String patientDetails = gson.toJson(patient);
            JsonObject patientDetailObject = new JsonParser().parse(patientDetails).getAsJsonObject();
            returnObject.add("userData",patientDetailObject);
            returnObject.addProperty("message","Successfully Logged In");
        } else {
            returnObject.addProperty("message","Login Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/signup")
    public Response patientSignUp(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        String nic = jsonObject.get("nic").getAsString();
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String email = jsonObject.get("email").getAsString();
        String birthday = jsonObject.get("birthday").getAsString();
        String mobile = jsonObject.get("mobile").getAsString();
        String emergency_contact = jsonObject.get("emergency_contact").getAsString();
        String password = jsonObject.get("password").getAsString();
        String image;
        if(jsonObject.get("image") != null){
            image = jsonObject.get("image").getAsString();
        }else {
            image = null;
        }
        Patient patient = new Patient(nic,name,gender,email,birthday,mobile,emergency_contact,password,image);
        PatientController patientController = new PatientController();
        boolean saveResult = patientController.savePatient(patient);
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("success",saveResult);
        if(saveResult){
            Patient savedPatient = patientController.getPatiaentDetails(nic);
            String patientDetails = gson.toJson(savedPatient);
            JsonObject patientDetailObject = new JsonParser().parse(patientDetails).getAsJsonObject();
            returnObject.add("userData",patientDetailObject);
            returnObject.addProperty("message","Successfully Saved");
        }
        else {
            returnObject.addProperty("message","Saving Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/patients")
    public Response allPatients() {
        Gson gson = new Gson();
        PatientController patientController = new PatientController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Patient> patientsList = patientController.getAllPatiaents();
        JsonArray patientArray = gson.toJsonTree(patientsList).getAsJsonArray();
        returnObject.add("patientList",patientArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

}
