package lk.ac.mrt.cse.medipal.controller;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;



public class ScoreCalculationController {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(ScoreCalculationController.class);

    static DrugController drugController = new DrugController();
    static HashMap<String, Integer> drugToDrugConflictScore = drugController.getDrugToDrugConflictScore();
    static HashMap<String, Integer> drugToDiseaseConflictScore = drugController.getDrugToDiseaseConflictScore();

    public int getConflictScore(String drug1, String drug2){

        if(drugToDrugConflictScore.get(drug1+"_"+drug2) != null)
            return drugToDrugConflictScore.get(drug1+"_"+drug2);
        else if(drugToDrugConflictScore.get(drug2+"_"+drug1) != null)
            return drugToDrugConflictScore.get(drug2+"_"+drug1);
        else if(drugToDiseaseConflictScore.get(drug1+"_"+drug2) != null)
            return drugToDiseaseConflictScore.get(drug1+"_"+drug2);
        else if(drugToDiseaseConflictScore.get(drug2+"_"+drug1) != null )
            return drugToDiseaseConflictScore.get(drug2+"_"+drug1);

        return 0;
    }




    /** Function to split parent matrix into child matrices **/

    public void split(String[][] P, String[][] C, int iB, int jB)

    {

        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)

            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)

                C[i1][j1] = P[i2][j2];

    }



    public int getBestPathId(String[][] pathList, String[][] patientHistory){

        int size = pathList[0].length;
        String[] bestPath = null;
        int[][] scoreMatrix = new int[size][size];
        int[][] finalScoreMatrix = new int[size][1];
        Integer[] transpose = new Integer[size];
        int index;

        ArrayList<String[][]> subMatrices_A = new ArrayList<>();
        ArrayList<String[][]> subMatrices_B = new ArrayList<>();

        String[][] A11 = new String[size/2][size/2];
        subMatrices_A.add(A11);
        String[][] A12 = new String[size/2][size/2];
        subMatrices_A.add(A12);
        String[][] A21 = new String[size/2][size/2];
        subMatrices_A.add(A21);
        String[][] A22 = new String[size/2][size/2];
        subMatrices_A.add(A22);
        String[][] B11 = new String[size/2][size/2];
        subMatrices_B.add(B11);
        String[][] B12 = new String[size/2][size/2];
        subMatrices_B.add(B12);
        String[][] B21 = new String[size/2][size/2];
        subMatrices_B.add(B21);
        String[][] B22 = new String[size/2][size/2];
        subMatrices_B.add(B22);

        //Splitting the matrix 1 into 4 halves
        split(pathList, A11, 0 , 0);

        split(pathList, A12, 0 , size/2);

        split(pathList, A21, size/2, 0);

        split(pathList, A22, size/2, size/2);


        //Splitting matrix 2 into 4 halves
        split(patientHistory, B11, 0 , 0);

        split(patientHistory, B12, 0 , size/2);

        split(patientHistory, B21, size/2, 0);

        split(patientHistory, B22, size/2, size/2);





        int threadcount = 0;
        Thread[] thread = new Thread[size*size];

        try {
            for(int i=0; i < size/2; i++) {

                for(int j=0; j<size/2; j++ ) {
                thread[threadcount] = new Thread(new WorkerThread(i, j, subMatrices_A.get(i), subMatrices_B.get(j), scoreMatrix, finalScoreMatrix));
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

        for(int i=0; i < size; i++){
            if(scoreMatrix[0][i] <= THRESHOLD){
                conflictedDrugs.add(patientHistory[0][i]);
            }
            if(finalScoreMatrix[i][0] > THRESHOLD){
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
