package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.*;
import lk.ac.mrt.cse.medipal.model.NotificationResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/notifications")
public class NotificationResource {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{user_id}")
    public Response sharedNotification(@PathParam("user_id") String user_id) {
        Gson gson = new Gson();
        NotificationResponseController notificationResponseController = new NotificationResponseController();
        NotificationResponse notificationResponse = notificationResponseController.getNotificationResponse(user_id);
        JsonObject returnObject = new JsonObject();

        String notificationDetails = gson.toJson(notificationResponse);
        returnObject = new JsonParser().parse(notificationDetails).getAsJsonObject();

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
