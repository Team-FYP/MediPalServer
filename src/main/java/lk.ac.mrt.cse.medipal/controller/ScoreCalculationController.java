package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.Database.ConflictScore;
import lk.ac.mrt.cse.medipal.model.ConflictScoreValue;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.Map.Entry;

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

    /*public int getBestPathId(String[][] pathList, String[][] patientHistory){

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
    }*/


    public int getBestPathId(String[][] pathList, String[][] patientHistory) {

        ScoreCalculationController scoreCalculationController = new ScoreCalculationController();
        int size = pathList[0].length;
        int[][] scoreMatrix = new int[size][size];
        int[][] finalScoreMatrix = new int[size][1];
        Integer[] transpose = new Integer[size];
        int index;

        IntStream.range(0, size).parallel().forEach(i->{
            for(int j=0; j<size; j++){
                for(int k=0; k<size; k++){
                    scoreMatrix[i][j] += scoreCalculationController.getConflictScore(pathList[i][k], patientHistory[k][j]);
                }
            }
        });

        IntStream.range(0, size).parallel().forEach(i->{
            for(int j=0; j<size; j++){
                finalScoreMatrix[i][0] += scoreMatrix[i][j];
            }
        });

        IntStream.range(0, size).parallel().forEach(i->{
            transpose[i] = finalScoreMatrix[i][0];
        });

        index = Arrays.asList(transpose).indexOf(Collections.max(Arrays.asList(transpose)));

        return index;

    }


    private static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();

        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



    public Object[] getConflictsWithSuggestions(String[][] pathList, String[][] patientHistory, int index) {

        ScoreCalculationController scoreCalculationController = new ScoreCalculationController();
        ConflictScoreValue conflictScoreValue;
        int size = pathList[0].length;
        ArrayList<ConflictScoreValue> conflictedDrugs = new ArrayList<>();
        ArrayList<String> sugestedDrugs = new ArrayList<>();
        Map<String, Integer> scoreMap = new HashMap<String, Integer>();
        int[][] scoreMatrix = new int[size][size];
        int[][] finalScoreMatrix = new int[size][1];
        final int THRESHOLD = -5000;


        IntStream.range(0, size).parallel().forEach(i->{
            for(int j=0; j<size; j++){
                for(int k=0; k<size; k++){
                    scoreMatrix[i][j] += scoreCalculationController.getConflictScore(pathList[i][k], patientHistory[k][j]);
                }
            }
        });

        IntStream.range(0, size).parallel().forEach(i->{
            for(int j=0; j<size; j++){
                finalScoreMatrix[i][0] += scoreMatrix[i][j];
            }
        });


        if(finalScoreMatrix[0][0] <= THRESHOLD){
            LOGGER.info("pass the THRESHOLD ...");
            for(int i=0; i < size; i++){
                if(patientHistory[0][i] != null || scoreMatrix[0][i] != 0){
                    LOGGER.info("drug: " + patientHistory[0][i]);
                    LOGGER.info("score: " + scoreMatrix[0][i]);
                    conflictScoreValue = new ConflictScoreValue();
                    conflictScoreValue.setDrugName(patientHistory[0][i]);
                    conflictScoreValue.setScore(Math.abs(scoreMatrix[0][i]));
                    conflictedDrugs.add(conflictScoreValue);
                }
            }
        }

        for(int i=1; i < size; i++){
            if(finalScoreMatrix[i][0] > THRESHOLD){
                if(pathList[i][index] != null){
                    scoreMap.put(pathList[i][index], finalScoreMatrix[i][0]);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : sortByValues(scoreMap).entrySet()) {
            sugestedDrugs.add(entry.getKey());
        }

        for(ConflictScoreValue conflictScoreValue1 : conflictedDrugs){
            LOGGER.info("name: " + conflictScoreValue1.getDrugName());
            LOGGER.info("score: " + conflictScoreValue1.getScore());

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
