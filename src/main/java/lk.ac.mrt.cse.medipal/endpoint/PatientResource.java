package lk.ac.mrt.cse.medipal.endpoint;

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
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String result = "";
//        try {
//            result = new DeviceHandler().getDeviceList(app_id);
//        } catch (IdeabizException e) {
//            logger.error("Ideabiz error:"+e.getMessage());
//        }
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/signup")
    public Response patientSignUp(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        String nic = jsonObject.get("nic").getAsString();
        String name = jsonObject.get("name").getAsString();
        String gender = jsonObject.get("gender").getAsString();
        String email = jsonObject.get("email").getAsString();
        String birthday = jsonObject.get("birthday").getAsString();
        String mobile = jsonObject.get("mobile").getAsString();
        String emergency_contact = jsonObject.get("emergency_contact").getAsString();
        String password = jsonObject.get("password").getAsString();
        String image = jsonObject.get("image").getAsString();
        Patient patient = new Patient(nic,name,gender,email,birthday,mobile,emergency_contact,password,image);
        PatientController patientController = new PatientController();
        boolean saveResult = patientController.savePatient(patient);
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("success",saveResult);
        returnObject.addProperty("message","Successfully Signed Up");
//        try {
//            result = new DeviceHandler().getDeviceList(app_id);
//        } catch (IdeabizException e) {
//            logger.error("Ideabiz error:"+e.getMessage());
//        }
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

}
