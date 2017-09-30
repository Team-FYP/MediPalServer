package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    public Response getBusList(String request) {
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

}
