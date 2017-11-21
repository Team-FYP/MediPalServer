package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.ConflictScore;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ScoreCalculationController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(ScoreCalculationController.class);


    public int getConflictScore(String drug1, String drug2){

        try {
            if(ConflictScore.getConflictScore().getDrugToDrugConflictScore().get(drug1+"_"+drug2) != null)
                return ConflictScore.getConflictScore().getDrugToDrugConflictScore().get(drug1+"_"+drug2);
            else if(ConflictScore.getConflictScore().getDrugToDrugConflictScore().get(drug2+"_"+drug1) != null)
                return ConflictScore.getConflictScore().getDrugToDrugConflictScore().get(drug2+"_"+drug1);
            else if(ConflictScore.getConflictScore().getDrugToDiseaseConflictScore().get(drug1+"_"+drug2) != null)
                return ConflictScore.getConflictScore().getDrugToDiseaseConflictScore().get(drug1+"_"+drug2);
            else if(ConflictScore.getConflictScore().getDrugToDiseaseConflictScore().get(drug2+"_"+drug1) != null )
                return ConflictScore.getConflictScore().getDrugToDiseaseConflictScore().get(drug2+"_"+drug1);
        } catch (SQLException e) {
            LOGGER.error("Error geting conflict scores", e);
        } catch (PropertyVetoException e) {
            LOGGER.error("Error geting conflict scores", e);
        } catch (IOException e) {
            LOGGER.error("Error geting conflict scores", e);
        }

        return 0;
    }

    public int getBestPathId(String[][] pathList, String[][] patientHistory){

        int size = pathList[0].length;
        String[] bestPath = null;
        int[][] scoreMatrix = new int[size][size];
        int[][] finalScoreMatrix = new int[size][1];
        Integer[] transpose = new Integer[size];
        int index;

        int threadcount = 0;
        Thread[] thread = new Thread[size*size];

        try {
            for(int i=0; i < size; i++) {
                for(int j=0; j<size; j++ ) {
                    thread[threadcount] = new Thread(new WorkerThread(i, j, pathList, patientHistory, scoreMatrix, finalScoreMatrix));
                    thread[threadcount].start();

                    thread[threadcount].join();
                    threadcount++;
                }

            }

        }
        catch (InterruptedException ie){

        }

        for(int i=0; i<size; i++){
            LOGGER.info(finalScoreMatrix[i][0]);
        }

        for(int i=0; i<size; i++){
            transpose[i] = finalScoreMatrix[i][0];
        }

        index = Arrays.asList(transpose).indexOf(Collections.max(Arrays.asList(transpose)));

        return index;
    }


    public Object[] getConflictsWithSuggestions(String[][] pathList, String[][] patientHistory, int index){

        int size = pathList[0].length;
        ArrayList<String> conflictedDrugs = new ArrayList<>();
        ArrayList<String> sugestedDrugs = new ArrayList<>();
        int[][] scoreMatrix = new int[size][size];
        int[][] finalScoreMatrix = new int[size][1];
        final int THRESHOLD = -5000;

        int threadcount = 0;
        Thread[] thread = new Thread[size*size];

        try {
            for(int i=0; i < size; i++) {
                for(int j=0; j<size; j++ ) {
                    thread[threadcount] = new Thread(new WorkerThread(i, j, pathList, patientHistory, scoreMatrix, finalScoreMatrix));
                    thread[threadcount].start();

                    thread[threadcount].join();
                    threadcount++;
                }
            }
        }
        catch (InterruptedException ie){

        }
        LOGGER.info("testing inside calculations");
        LOGGER.info("index:" + index);
        for (int a=0; a<pathList.length; a++){
            for(int b=0; b<pathList[0].length; b++){
                LOGGER.info(pathList[a][b]);
            }
        }
        LOGGER.info("scores:");
        for (int a=0; a<finalScoreMatrix.length; a++){

            LOGGER.info(finalScoreMatrix[a][0]);

        }


        for(int i=0; i < size; i++){
            if(scoreMatrix[0][i] <= THRESHOLD){
                if(patientHistory[0][i] != null)
                    conflictedDrugs.add(patientHistory[0][i]);
            }
            if(finalScoreMatrix[i][0] > THRESHOLD){
                if(pathList[i][index] != null)
                    sugestedDrugs.add(pathList[i][index]);
            }
        }

        return new Object[]{conflictedDrugs, sugestedDrugs};
    }

}

class WorkerThread implements Runnable {

    private int row;
    private int col;
    private String A[][];
    private String B[][];
    private int C[][];
    private int D[][];


    public WorkerThread(int row, int col, String A[][], String B[][], int C[][], int D[][]) {
        this.row = row;
        this.col = col;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    @Override
    public void run() {
        ScoreCalculationController scoreCalculationController = new ScoreCalculationController();
        for(int k = 0; k < B.length; k++) {
            C[row][col] += scoreCalculationController.getConflictScore(A[row][k], B[k][col]);
        }
        D[row][0] = D[row][0] + C[row][col];

    }

}
