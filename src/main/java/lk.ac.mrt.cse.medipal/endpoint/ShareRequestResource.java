package lk.ac.mrt.cse.medipal.endpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.ac.mrt.cse.medipal.controller.ShareRequestController;
import lk.ac.mrt.cse.medipal.model.ShareRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        JsonObject returnObject = new JsonObject();
        boolean shareHistoryWithDoctor = shareRequestController.shareHistoryWithDoctor(shareRequest);
        if(shareHistoryWithDoctor){
            boolean shareHistoryNotification = shareRequestController.shareHistoryNotification(shareRequest);
            returnObject.addProperty("success",shareHistoryNotification);
            if(shareHistoryNotification){
                returnObject.addProperty("message","Successfully Shared History With Doctor");
            }
        }else {
            returnObject.addProperty("message","Failed Sharing History with Doctor");
        }

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
