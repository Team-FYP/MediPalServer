package lk.ac.mrt.cse.medipal.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ScoreCalculationController {
    private static final int MATRIX_SIZE = 1024,
            POOL_SIZE = Runtime.getRuntime().availableProcessors(), MINIMUM_THRESHOLD = 64;
    private final ExecutorService exec = Executors.newFixedThreadPool(POOL_SIZE);
    String[][] pathwayMat;
    String[][] historyMat;
    int[][] conflictMat;

    //Transforming the matrices into similar size
    public Object[] squareMatTransformation(String[][] pathwayMat, String[] historyArr){

        int numEleInHistoryMat = historyArr.length;
        int numOfPathways = pathwayMat.length;
        int maxNoOfEleInPathway = pathwayMat[0].length;
        String[][] historyMat;
        String[][] pathwayMatTrans;
        int matSize;

        //When number of historical elelments is the matrix size
        if(numEleInHistoryMat > numOfPathways && numEleInHistoryMat > maxNoOfEleInPathway){

            matSize = numEleInHistoryMat;
            //transforming the pathway matrix
            pathwayMatTrans = new String[matSize][matSize];
            for (int i=0; i<matSize;i++){
                for (int j = 0; j < matSize; j++) {
                    if(i<numOfPathways && j<maxNoOfEleInPathway)
                        pathwayMatTrans[i][j] = pathwayMat[i][j];
                    else
                        pathwayMatTrans[i][j] = "Null";
                    }
                }


            //transforming the history matrix
            historyMat = new String[matSize][matSize];
            for (int i=0;i<matSize; i++){
                for (int j=0; j<matSize;j++){
                    historyMat[i][j] = historyArr[j];
                }
            }

        }

        //when no. of pathways is the matrix size
        else if(numOfPathways>numEleInHistoryMat && numOfPathways>maxNoOfEleInPathway) {
            matSize = numOfPathways;
            //transforming the pathway matrix
            pathwayMatTrans = new String[matSize][matSize];
            for (int i = 0; i < matSize; i++) {
                for (int j = 0; j < matSize; j++) {
                    if (j < maxNoOfEleInPathway)
                        pathwayMatTrans[i][j] = pathwayMat[i][j];
                    else
                        pathwayMatTrans[i][j] = "Null";
                }


            }

            //transforming the history matrix
            historyMat = new String[matSize][matSize];
            for (int i=0;i<matSize; i++){
                for (int j=0; j<matSize;j++){
                    if(j<numEleInHistoryMat)
                        historyMat[i][j] = historyArr[j];
                    else
                        historyMat[i][j] = "Null";
                }
            }
        }

        //when maximum number of elements in pathway matrix is the matrix size
        else if(maxNoOfEleInPathway>numOfPathways && maxNoOfEleInPathway>numEleInHistoryMat){

            matSize = maxNoOfEleInPathway;

            //transforming the pathway matrix
            pathwayMatTrans = new String[matSize][matSize];
            for (int i = 0; i < matSize; i++) {
                for (int j = 0; j < matSize; j++) {
                    if (i < numOfPathways)
                        pathwayMatTrans[i][j] = pathwayMat[i][j];
                    else
                        pathwayMatTrans[i][j] = "Null";
                }


            }

            //transforming the history matrix
            historyMat = new String[matSize][matSize];
            for (int i=0;i<matSize; i++){
                for (int j=0; j<matSize;j++){
                    if(j<numEleInHistoryMat)
                        historyMat[i][j] = historyArr[j];
                    else
                        historyMat[i][j] = "Null";
                }
            }

        }

        return new Object[]{pathwayMatTrans,historyMat};

    }

/*
    //Matrx multiplication method
    public int[][] matrixMul(String[][] pathwayMat, String[][] historyMat){

        this.pathwayMat = pathwayMat;
        this.historyMat = historyMat;
        this.conflictMat = new int[pathwayMat.length][pathwayMat.length];
        String[] comparingStrings = new String[2];

        for (int i=0; i< pathwayMat.length;i++){

            for (int j=0; j<pathwayMat.length ;j++){

                for (int k = 0; k < pathwayMat.length; k++) {
                    comparingStrings[0] = pathwayMat[i][k];
                    comparingStrings[1] = historyMat[k][j];
                    conflictMat[i][k] += getConflictValue(comparingStrings);//pass the hash map parameter
                }
            }

        }

        return conflictMat;

    }
    */

    //Parallel multiplication happens here
    public void multiply() {
        //multiplyRecursive(0, 0, 0, 0, 0, 0, pathwayMat.length);
        Future f = exec.submit(new MultiplyTask(pathwayMat, historyMat, conflictMat, 0, 0, 0, 0, 0, 0, pathwayMat.length));
        try {
            f.get();
            exec.shutdown();
        } catch (Exception e) {

        }
    }

    public int[][] getConflictMat(){
        return conflictMat;
    }

    public int getConflictValue(String[] comparingStrings){


        int conflictVal = 0;//get value from hashMap

        //db call method

        return conflictVal;


    }

    class MultiplyTask implements Runnable{
        String[] comparingStrings = new String[2];
        private String[][] pathwayMat;
        private String[][] historyMat;
        private int[][] conflictMat;
        private int pathwayMat_i, pathwayMat_j, historyMat_i, historyMat_j, conflictMat_i, conflictMat_j, size;

        MultiplyTask(String[][] pathwayMat, String[][] historyMat, int[][] conflictMat, int pathwayMat_i, int pathwayMat_j, int historyMat_i, int historyMat_j, int c_i, int c_j, int size) {
            this.pathwayMat = pathwayMat;
            this.historyMat = historyMat;
            this.conflictMat = conflictMat;
            this.pathwayMat_i = pathwayMat_i;
            this.pathwayMat_j = pathwayMat_j;
            this.historyMat_i = historyMat_i;
            this.historyMat_j = historyMat_j;
            this.conflictMat_i = conflictMat_i;
            this.conflictMat_j = conflictMat_j;
            this.size = size;
        }

        public void run() {
            //System.out.format("[%d,%d]x[%d,%d](%d)\n",a_i,a_j,b_i,b_j,size);
            int h = size/2;
            if (size <= MINIMUM_THRESHOLD) {
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        for (int k = 0; k < size; ++k) {
                            //c[c_i+i][c_j+j] += a[a_i+i][a_j+k] * b[b_i+k][b_j+j];
                            comparingStrings[0] = pathwayMat[pathwayMat_i+i][pathwayMat_j+k];
                            comparingStrings[1] = historyMat[historyMat_i+k][historyMat_j+j];
                            conflictMat[conflictMat_i+i][conflictMat_j+j] += getConflictValue(comparingStrings);//pass the hash map parameter
                        }
                    }
                }
            } else {
                MultiplyTask[] tasks = {
                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i, pathwayMat_j, historyMat_i, historyMat_j, conflictMat_i, conflictMat_j, h),
                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i, pathwayMat_j+h, historyMat_i+h, historyMat_j, conflictMat_i, conflictMat_j, h),

                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i, pathwayMat_j, historyMat_i, historyMat_j+h, conflictMat_i, conflictMat_j+h, h),
                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i, pathwayMat_j+h, historyMat_i+h, historyMat_j+h, conflictMat_i, conflictMat_j+h, h),

                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i+h, pathwayMat_j, historyMat_i, historyMat_j, conflictMat_i+h, conflictMat_j, h),
                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i+h, pathwayMat_j+h, historyMat_i+h, historyMat_j, conflictMat_i+h, conflictMat_j, h),

                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i+h, pathwayMat_j, historyMat_i, historyMat_j+h, conflictMat_i+h, conflictMat_j+h, h),
                        new MultiplyTask(pathwayMat, historyMat, conflictMat, pathwayMat_i+h, pathwayMat_j+h, historyMat_i+h, historyMat_j+h, conflictMat_i+h, conflictMat_j+h, h)
                };

                FutureTask[] fs = new FutureTask[tasks.length/2];

                for (int i = 0; i < tasks.length; i+=2) {
                    fs[i/2] = new FutureTask(new Sequentializer(tasks[i], tasks[i+1]), null);
                    exec.execute(fs[i/2]);
                }
                for (int i = 0; i < fs.length; ++i) {
                    fs[i].run();
                }
                try {
                    for (int i = 0; i < fs.length; ++i) {
                        fs[i].get();
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    class Sequentializer implements Runnable{
        private MultiplyTask first, second;
        Sequentializer(MultiplyTask first, MultiplyTask second) {
            this.first = first;
            this.second = second;
        }
        public void run() {
            first.run();
            second.run();
        }

    }

}
