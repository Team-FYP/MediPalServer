package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.ShareNotificationController;
import lk.ac.mrt.cse.medipal.controller.ShareRequestController;
import lk.ac.mrt.cse.medipal.model.ShareNotification;
import lk.ac.mrt.cse.medipal.model.ShareRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/share")
public class ShareRequestResource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/sharehistory")
    public Response login(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Gson gson = new Gson();
        ShareRequest shareRequest = gson.fromJson(jsonObject.toString(), ShareRequest.class);
        ShareRequestController shareRequestController = new ShareRequestController();
        ShareNotificationController shareNotificationController = new ShareNotificationController();
        JsonObject returnObject = new JsonObject();
        boolean shareHistoryWithDoctor = shareRequestController.shareHistoryWithDoctor(shareRequest);
        if(shareHistoryWithDoctor){
            boolean shareHistoryNotification = shareNotificationController.addShareHistoryNotification(shareRequest);
            returnObject.addProperty("success",shareHistoryNotification);
            if(shareHistoryNotification){
                returnObject.addProperty("message","Successfully Shared History With Doctor");
            }
        }else {
            returnObject.addProperty("message","Failed Sharing History with Doctor");
        }

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}/notfication")
    public Response sharedNotification(@PathParam("id") String doctor_id) {
        Gson gson = new Gson();
        ShareNotificationController shareNotificationController = new ShareNotificationController();
        JsonObject returnObject = new JsonObject();
        ArrayList<ShareNotification> shareRequestArrayList = shareNotificationController.getAllSharedNotifications(doctor_id);
        JsonArray sharedRequestsArray = gson.toJsonTree(shareRequestArrayList).getAsJsonArray();
        returnObject.add("itemsList",sharedRequestsArray);

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}