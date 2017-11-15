package lk.ac.mrt.cse.medipal.endpoint;


import com.google.gson.JsonObject;
import lk.ac.mrt.cse.medipal.controller.ScoreCalculationController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Prescription Resource endpoint
 */
@Path("/scorecalculation")
public class ScoreCalculationResource {

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/testscore")
    public Response getScoreCalculation() {
        ScoreCalculationController scoreCalculationController = new ScoreCalculationController();

        String pathList[][] = new String[6][6];
        String patientHistory[][] = new String[6][6];

        for(int i =0; i<6;i++){
            for(int j =0; j<6;j++){
                if(j== 0){
                    pathList[i][j] = "Hypertension";
                }
                if(j== 1){
                    pathList[i][j] = "Monopril";
                }if(j== 2) {
                    pathList[i][j] = "Azor";
                }
            }
        }
        pathList[0][3] = "Diamox Squels";
        pathList[1][3] = "Diamox";
        pathList[2][3] = "Daranide";
        pathList[3][3] = "Keveyis";
        pathList[4][3] = "Neptazane";

        for(int i =0; i<6;i++){
            for(int j =0; j<6;j++){
                if(j== 0){
                    patientHistory[i][j] = "Diabetes";
                }
                if(j== 1){
                    patientHistory[i][j] = "metformin";
                }if(j== 2) {
                    patientHistory[i][j] = "Sitagliptin";
                }
            }
        }




        scoreCalculationController.multiplyMatrices(pathList, patientHistory);
        JsonObject returnObject = new JsonObject();

        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }
}
