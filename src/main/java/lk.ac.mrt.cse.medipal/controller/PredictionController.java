package lk.ac.mrt.cse.medipal.controller;

import com.google.gson.Gson;
import com.microsoft.z3.Z3Exception;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import org.apache.log4j.Logger;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PredictionController {

    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(PredictionController.class);

    private Object[] transformArrays(String[][] pathList, String[] patientHistory){

        int maximum = Math.max(Math.max(pathList.length, pathList[0].length), patientHistory.length);
        String[][] pathListMatix = new String[maximum][maximum];
        String[][] patientHistoryMatrix = new String[maximum][maximum];

        for(int i=0; i<pathList.length; i++){
            pathListMatix[i] = Arrays.copyOf(pathList[i], maximum);
        }

        for(int l=0; l<maximum; l++){
            patientHistoryMatrix[l] = Arrays.copyOf(patientHistory, maximum);
        }

        return new Object[]{pathListMatix, patientHistoryMatrix};

    }

    public String[] getLevelUpDrugs(String patientID, String disease) throws Z3Exception, DiseasePathController.TestFailedException {
        int i=1;
        int j=0;

        HashMap<String, Integer> diseaseMap = new HashMap<>();
        diseaseMap.put("Diabetes", 1);
        diseaseMap.put("Hypertension", 2);
        diseaseMap.put("COPD", 3);
        DrugController drugController = new DrugController();
        ArrayList<String> patientHistory = new ArrayList<>();
        ScoreCalculationController calculationController = new ScoreCalculationController();
        DiseaseController diseaseController = new DiseaseController();
        DiseasePathController pathController = new DiseasePathController();
        PrescriptionController prescriptionController = new PrescriptionController();
        Prescription prescription = prescriptionController.getLastPrescriptionForDisease(patientID, diseaseController.getDiseaseId(disease));
        String[] prescribeDrugs;
        if(prescription == null){
            prescribeDrugs = new String[1];
            prescribeDrugs[0] = disease;
        }else {
            ArrayList<PrescriptionDrug> prescriptionDrugs = prescription.getPrescription_drugs();
            prescribeDrugs = new String[prescriptionDrugs.size() + 1];
            prescribeDrugs[0] = disease;
            for (PrescriptionDrug prescriptionDrug : prescriptionDrugs) {
                LOGGER.info(i);
                prescribeDrugs[i] = prescriptionDrug.getDrug().getDrug_name();
                LOGGER.info(prescribeDrugs[i]);
                i++;
            }
        }
        for(int k=1; k<=3; k++){
            Prescription prescription1 = prescriptionController.getLastPrescriptionForDisease(patientID, k);
            if(prescription1 != null && k==1){
                patientHistory.add("Diabetes");
                ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
                for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                    patientHistory.add(prescriptionDrug.getDrug().getDrug_name());

                }
            }else if(prescription1 != null && k==2){
                patientHistory.add("Hypertension");
                ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
                for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                    patientHistory.add(prescriptionDrug.getDrug().getDrug_name());

                }
            }else if(prescription1 != null && k==3){
                LOGGER.info("inside COPD");
                LOGGER.info("size"+prescription1.getPrescription_drugs().size());
                patientHistory.add("COPD");
                ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
                for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                    patientHistory.add(prescriptionDrug.getDrug().getDrug_name());

                }
            }

        }

        ArrayList<String> list = drugController.getCategoryListByDrugList(diseaseMap.get(disease), new ArrayList<String>(Arrays.asList(prescribeDrugs)));
        list.add(disease);
        String[] prescribeCategory = list.toArray(new String[list.size()]);

        String[][] pathList = pathController.findDiseaseLevelUpPathList(disease, prescribeCategory);

        Object[] arrays = transformArrays(pathList, patientHistory.toArray(new String[patientHistory.size()]));

        return pathList[calculationController.getBestPathId((String[][])arrays[0], (String[][])arrays[1])];
    }

    public String[] getLevelDownDrugs(String patientID, String disease) throws Z3Exception, DiseasePathController.TestFailedException {
        int i=1;
        int j=0;

        HashMap<String, Integer> diseaseMap = new HashMap<>();
        diseaseMap.put("Diabetes", 1);
        diseaseMap.put("Hypertension", 2);
        diseaseMap.put("COPD", 3);
        DrugController drugController = new DrugController();
        ArrayList<String> patientHistory = new ArrayList<>();
        ScoreCalculationController calculationController = new ScoreCalculationController();
        DiseaseController diseaseController = new DiseaseController();
        DiseasePathController pathController = new DiseasePathController();
        PrescriptionController prescriptionController = new PrescriptionController();
        Prescription prescription = prescriptionController.getLastPrescriptionForDisease(patientID, diseaseController.getDiseaseId(disease));
        ArrayList<PrescriptionDrug> prescriptionDrugs = prescription.getPrescription_drugs();
        String[] prescribeDrugs = new String[prescriptionDrugs.size() + 1];
        prescribeDrugs[0] = disease;
        for(PrescriptionDrug prescriptionDrug : prescriptionDrugs){
            prescribeDrugs[i] = prescriptionDrug.getDrug().getDrug_name();
            i++;
        }
        for(int k=1; k<=3; k++){
            Prescription prescription1 = prescriptionController.getLastPrescriptionForDisease(patientID, k);
            if(prescription1 != null && k==1){
                patientHistory.add("Diabetes");
            }else if(prescription1 != null && k==2){
                patientHistory.add("Hypertension");
            }else if(prescription1 != null && k==3){
                patientHistory.add("COPD");
            }
            ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
            for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                patientHistory.add(prescriptionDrug.getDrug().getDrug_name());
            }
        }

        ArrayList<String> list = drugController.getCategoryListByDrugList(diseaseMap.get(disease), new ArrayList<String>(Arrays.asList(prescribeDrugs)));
        list.add(disease);
        String[] prescribeCategory = list.toArray(new String[list.size()]);

        String[][] pathList = pathController.findDiseaseLevelDownPathList(disease, prescribeCategory);

        Object[] arrays = transformArrays(pathList, patientHistory.toArray(new String[patientHistory.size()]));

        return pathList[calculationController.getBestPathId((String[][])arrays[0], (String[][])arrays[1])];
    }


    public Object[] getSuggestedDrugs(String patientID, String disease, String[] prescribedDrugs, String changedDrug) throws Z3Exception, DiseasePathController.TestFailedException {
        int i=1;
        int j=0;
        int index = Arrays.asList(prescribedDrugs).indexOf(changedDrug);
        HashMap<String, Integer> diseaseMap = new HashMap<>();
        diseaseMap.put("Diabetes", 1);
        diseaseMap.put("Hypertension", 2);
        diseaseMap.put("COPD", 3);
        ArrayList<String> patientHistory = new ArrayList<>();
        ScoreCalculationController calculationController = new ScoreCalculationController();
        DiseasePathController pathController = new DiseasePathController();
        PrescriptionController prescriptionController = new PrescriptionController();

        for(int k=1; k<=3; k++){
            Prescription prescription1 = prescriptionController.getLastPrescriptionForDisease(patientID, k);
            if(prescription1 != null && k==1){
                patientHistory.add("Diabetes");
                ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
                for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                    patientHistory.add(prescriptionDrug.getDrug().getDrug_name());
                }
            }else if(prescription1 != null && k==2){
                patientHistory.add("Hypertension");
                ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
                for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                    patientHistory.add(prescriptionDrug.getDrug().getDrug_name());
                }
            }else if(prescription1 != null && k==3){
                patientHistory.add("COPD");
                ArrayList<PrescriptionDrug> prescriptionDrugs1 = prescription1.getPrescription_drugs();
                for(PrescriptionDrug prescriptionDrug : prescriptionDrugs1){
                    patientHistory.add(prescriptionDrug.getDrug().getDrug_name());
                }
            }


        }

        for(String drug : prescribedDrugs){
            if(!patientHistory.contains(drug))
                patientHistory.add(drug);
        }
        String[][] pathList = pathController.findSuggestionPathList(disease, prescribedDrugs, changedDrug);

        Object[] arrays = transformArrays(pathList, patientHistory.toArray(new String[patientHistory.size()]));

        return calculationController.getConflictsWithSuggestions((String[][])arrays[0], (String[][])arrays[1], index);
    }

}
