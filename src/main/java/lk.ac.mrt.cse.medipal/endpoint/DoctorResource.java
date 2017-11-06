package lk.ac.mrt.cse.medipal.endpoint;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import org.apache.log4j.Logger;

import javax.print.Doc;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Doctor Resource endpoint
 */
@Path("/doctor")
public class DoctorResource {
    public static Logger logger = Logger.getLogger(DoctorResource.class);
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/login")
    public Response login(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        DoctorController doctorController = new DoctorController();
        boolean result = doctorController.checkLogin(username,password);
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("success",result);
        if (result) {
            Doctor doctor = doctorController.getDoctorDetails(username);
            String doctorDetails = gson.toJson(doctor);
            JsonObject doctorDetailObject = new JsonParser().parse(doctorDetails).getAsJsonObject();
            returnObject.add("userData",doctorDetailObject);
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
    public Response doctorSignUp(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        String registration_id = jsonObject.get("registration_id").getAsString();
        String speciality = jsonObject.get("speciality").getAsString();
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String email = jsonObject.get("email").getAsString();
        String mobile = jsonObject.get("mobile").getAsString();
        String password = jsonObject.get("password").getAsString();
        String image;
        if(jsonObject.get("image") != null){
            image = jsonObject.get("image").getAsString();
        }else {
            image = null;
        }
        Doctor doctor = new Doctor(registration_id, speciality,name,gender,email,mobile,password,image);
        DoctorController doctorController = new DoctorController();
        boolean checkDuplicateDoctor = doctorController.checkDuplicateDoctor(registration_id);
        JsonObject returnObject = new JsonObject();
        if(checkDuplicateDoctor){
            returnObject.addProperty("success",!checkDuplicateDoctor);
            returnObject.addProperty("message","User with same ID exists");
            return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
        }
        boolean saveResult = doctorController.saveDoctor(doctor);
        returnObject.addProperty("success",saveResult);
        if(saveResult){
            Doctor savedDoctor = doctorController.getDoctorDetails(registration_id);
            String doctorDetails = gson.toJson(savedDoctor);
            JsonObject doctorDetailObject = new JsonParser().parse(doctorDetails).getAsJsonObject();
            returnObject.add("userData",doctorDetailObject);
            returnObject.addProperty("message","Successfully Saved");
        }else {
            returnObject.addProperty("message","Saving Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/doctors")
    public Response allDoctors() {
        Gson gson = new Gson();
        DoctorController doctorController = new DoctorController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Doctor> doctorsList = doctorController.getAllDoctors();
        JsonArray doctorsArray = gson.toJsonTree(doctorsList).getAsJsonArray();
        returnObject.add("doctorsList",doctorsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}/patients")
    public Response allPatientsOfDoctor(@PathParam("id") String doctorID) {
        Gson gson = new Gson();
        DoctorController doctorController = new DoctorController();
        JsonObject returnObject = new JsonObject();
        ArrayList<Patient> patientsList = doctorController.getPatiaentDetailsByDoctorID(doctorID);
        JsonArray patientArray = gson.toJsonTree(patientsList).getAsJsonArray();
        returnObject.add("items",patientArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/update")
    public Response doctorUpdate(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        String registration_id = jsonObject.get("registration_id").getAsString();
        String speciality = jsonObject.get("speciality").getAsString();
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String email = jsonObject.get("email").getAsString();
        String mobile = jsonObject.get("mobile").getAsString();
        String password = jsonObject.get("password").getAsString();
        String image;
        if(jsonObject.get("image") != null){
            image = jsonObject.get("image").getAsString();
        }else {
            image = null;
        }
        Doctor doctor = new Doctor(registration_id, speciality,name,gender,email,mobile,password,image);
        DoctorController doctorController = new DoctorController();
        JsonObject returnObject = new JsonObject();
        boolean updateResult = doctorController.updateDoctor(doctor);
        returnObject.addProperty("success",updateResult);
        if(updateResult){
            Doctor updatedDoctor = doctorController.getDoctorDetails(registration_id);
            String doctorDetails = gson.toJson(updatedDoctor);
            JsonObject doctorDetailObject = new JsonParser().parse(doctorDetails).getAsJsonObject();
            returnObject.add("userData",doctorDetailObject);
            returnObject.addProperty("message","Successfully Updated");
        }else {
            returnObject.addProperty("message","Updating Failed");
        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}