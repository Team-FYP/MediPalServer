package lk.ac.mrt.cse.medipal.endpoint;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
            returnObject.add("doctorData",doctorDetailObject);
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
        String registration_id = jsonObject.get("registration_id").getAsString();
        String speciality = jsonObject.get("speciality").getAsString();
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String email = jsonObject.get("email").getAsString();
        String contact_no = jsonObject.get("contact_no").getAsString();
        String password = jsonObject.get("password").getAsString();
        String image = jsonObject.get("image").getAsString();
        Doctor doctor = new Doctor(registration_id, speciality,name,gender,email,contact_no,password,image);
        DoctorController doctorController = new DoctorController();
        boolean saveResult = doctorController.saveDoctor(doctor);
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("success",saveResult);
        returnObject.addProperty("message","Successfully Signed Up");
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
