package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.*;
import lk.ac.mrt.cse.medipal.model.AllergyNotification;
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

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/seen/historyshared/{id}")
    public Response seenHistorySharedNotification(@PathParam("id") int notification_id) {
        ShareNotificationController shareNotificationController = new ShareNotificationController();
        JsonObject returnObject = new JsonObject();
        boolean updatedNotificationStatus = shareNotificationController.updateSharedNotificationsStatus(notification_id);
        returnObject.addProperty("success",updatedNotificationStatus);
        if(updatedNotificationStatus){
            returnObject.addProperty("message","Successfully Updated Message Status");
        }

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/seen/prescription/{id}")
    public Response seenPrescriptionNotification(@PathParam("id") int notification_id) {
        PrescriptionNotificationController prescriptionNotificationController = new PrescriptionNotificationController();
        JsonObject returnObject = new JsonObject();
        boolean updatedNotificationStatus = prescriptionNotificationController.updatePrescriptionNotificationsStatus(notification_id);
        returnObject.addProperty("success",updatedNotificationStatus);
        if(updatedNotificationStatus){
            returnObject.addProperty("message","Successfully Updated Message Status");
        }

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/seen/allergy/{id}")
    public Response seenAllergyNotification(@PathParam("id") int notification_id) {
        AllergyNotificationController allergyNotificationController = new AllergyNotificationController();
        JsonObject returnObject = new JsonObject();
        boolean updatedNotificationStatus = allergyNotificationController.updateAllergyNotificationsStatus(notification_id);
        returnObject.addProperty("success",updatedNotificationStatus);
        if(updatedNotificationStatus){
            returnObject.addProperty("message","Successfully Updated Message Status");
        }

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
