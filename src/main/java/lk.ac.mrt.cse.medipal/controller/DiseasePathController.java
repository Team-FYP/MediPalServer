package lk.ac.mrt.cse.medipal.controller;

import java.lang.reflect.Array;
import java.util.*;

import com.microsoft.z3.*;
import org.apache.log4j.Logger;

public class DiseasePathController {

    final String DIABETES = "Diabetes";
    final String METFORMIN = "metformin";
    final String SULFONYLUREA = "sulfonylurea";
    final String SITAGLIPTIN = "sitagliptin";
    final String PIOGLITAZONE = "pioglitazone";
    final String EMPAGLIFLOZIN = "empaglifozin";
    final String ACARBOSE = "acarbose";
    final String INSULIN = "insulin";

    final String COPD = "COPD";
    final String CORTICOSTEROID = "corticosteroid";
    final String SHORTBRONCHODILATOR = "short acting bronchodilators";
    final String LONGBRONCHODILATOR = "long acting bronchodilators";

    final String HYPERTENSION = "Hypertension";
    final String ACE = "ACE Inhabitors";
    final String ARB = "ARB";
    final String CCB = "CCB";
    final String DIURETIC = "Diuretic";
    final String ALPHA = "alpha-blockers";
    final String BETA = "beta-blockers";

    @SuppressWarnings("serial")
    public class TestFailedException extends Exception
    {
        public TestFailedException()
        {
            super("Check FAILED");
        }
    };

    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(DiseasePathController.class);

    DrugController drugController = new DrugController();

    private String[][] findDiabetesLevelUp(Context ctx, String[] prescribeDrugs) throws Z3Exception, TestFailedException {


        ArrayList<String> seletedMeds = new ArrayList<>();
        String[][] pathList = null;

        BoolExpr[] medi = new BoolExpr[8];
        medi[0] = ctx.MkBoolConst(DIABETES);
        medi[1] = ctx.MkBoolConst(METFORMIN);
        medi[2] = ctx.MkBoolConst(SULFONYLUREA);
        medi[3] = ctx.MkBoolConst(ACARBOSE);
        medi[4] = ctx.MkBoolConst(SITAGLIPTIN);
        medi[5] = ctx.MkBoolConst(EMPAGLIFLOZIN);
        medi[6] = ctx.MkBoolConst(PIOGLITAZONE);
        medi[7] = ctx.MkBoolConst(INSULIN);


        BoolExpr nd0 = ctx.MkBoolConst("nd0");
        BoolExpr nd1 = ctx.MkBoolConst("nd1");
        BoolExpr nd2 = ctx.MkBoolConst("nd2");
        BoolExpr nd3 = ctx.MkBoolConst("nd3");
        BoolExpr nd4 = ctx.MkBoolConst("nd4");

        ArrayList<String> list1 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(METFORMIN, SULFONYLUREA)));
        ArrayList<String> list2 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(ACARBOSE)));
        ArrayList<String> list3 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(SITAGLIPTIN, PIOGLITAZONE, EMPAGLIFLOZIN)));

        String[] node1 = list1.toArray(new String[list1.size()]);
        String[] node2 = list2.toArray(new String[list2.size()]);
        String[] node3 = list3.toArray(new String[list3.size()]);

        BoolExpr expNd0 = ctx.MkBoolConst("expNd0");
        BoolExpr expNd1 = ctx.MkBoolConst("expNd1");
        BoolExpr expNd2 = ctx.MkBoolConst("expNd2");
        BoolExpr expNd3 = ctx.MkBoolConst("expNd3");
        BoolExpr expNd4 = ctx.MkBoolConst("expNd4");
        BoolExpr expNd0R = ctx.MkBoolConst("expNd0R");
        BoolExpr expNd1R = ctx.MkBoolConst("expNd1R");
        BoolExpr expNd2R = ctx.MkBoolConst("expNd2R");
        BoolExpr expNd3R = ctx.MkBoolConst("expNd3R");
        BoolExpr expNd4R = ctx.MkBoolConst("expNd4R");
        BoolExpr exp1 = ctx.MkBoolConst("exp1");
        BoolExpr exp2 = ctx.MkBoolConst("exp2");
        BoolExpr exp3 = ctx.MkBoolConst("exp3");
        BoolExpr exp4 = ctx.MkBoolConst("exp4");

        for (String drug : prescribeDrugs) {
            if (drug.equalsIgnoreCase(DIABETES)) {
                medi[0] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(METFORMIN)) {
                medi[1] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(SULFONYLUREA)) {
                medi[2] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(ACARBOSE)) {
                medi[3] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(SITAGLIPTIN)) {
                medi[4] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(EMPAGLIFLOZIN)) {
                medi[5] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(PIOGLITAZONE)) {
                medi[6] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(INSULIN)) {
                medi[7] = ctx.MkTrue();
            }
        }

        expNd0 = ctx.MkImplies(medi[0], nd0);
        expNd1 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[1], medi[2]}), nd1);
        expNd2 = ctx.MkImplies(medi[3], nd2);
        expNd3 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[4], medi[5], medi[6]}), nd3);
        expNd4 = ctx.MkImplies(medi[7], nd4);

        expNd0R = ctx.MkImplies(nd0, medi[0]);
        expNd1R = ctx.MkImplies(nd1, ctx.MkAnd(new BoolExpr[]{medi[1], medi[2]}));
        expNd2R = ctx.MkImplies(nd2, medi[3]);
        expNd3R = ctx.MkImplies(nd3, ctx.MkAnd(new BoolExpr[]{medi[4], medi[5], medi[6]}));
        expNd4R = ctx.MkImplies(nd4, medi[7]);

        exp1 = ctx.MkImplies(nd0, ctx.MkAnd(new BoolExpr[]{nd1, nd2}));
        exp2 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd1, nd2}), nd0);
        exp3 = ctx.MkIff(nd1, nd3);
        exp4 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd2, nd3}), nd4);

        Solver s = ctx.MkSolver();

        s.Assert(expNd0);
        s.Assert(expNd1);
        s.Assert(expNd2);
        s.Assert(expNd3);
        s.Assert(expNd4);
        if (medi[4].IsTrue() || medi[5].IsTrue() || medi[6].IsTrue()) {
            s.Assert(exp4);
            s.Assert(exp3);
            s.Assert(exp2);
        } else if (medi[1].IsTrue() || medi[2].IsTrue()) {
            s.Assert(exp3);
            s.Assert(exp2);
        } else if (medi[3].IsTrue()) {
            s.Assert(exp4);
            s.Assert(exp2);
        } else if (medi[0].IsTrue()) {
            s.Assert(exp1);
        }

        s.Assert(expNd0R);
        s.Assert(expNd1R);
        s.Assert(expNd2R);
        s.Assert(expNd3R);
        s.Assert(expNd4R);

        if (s.Check() == Status.SATISFIABLE) {
            Model m = s.Model();

            int a = node1.length;
            int b = node2.length;
            int c = node3.length;
            int e;

            if (m.Evaluate(nd3, true).IsTrue() && m.Evaluate(nd4, true).IsTrue()) {
                LOGGER.info("nd3 and nd4");
                pathList = new String[a*c][4];
                for(int i=0; i<a; i++){
                    for(int j=0; j<c; j++){
                        e = (i * c) + j;
                        if(e < c*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        pathList[e][0] = DIABETES;
                        pathList[e][3] = INSULIN;
                        pathList[e][2] = node3[e%c];
                    }
                }

            } else if (m.Evaluate(nd3, true).IsTrue()) {
                LOGGER.info("nd3");
                pathList = new String[a*c][3];
                for(int i=0; i<a; i++){
                    for(int j=0; j<c; j++){
                        e = (i * c) + j;
                        if(e < c*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        pathList[e][0] = DIABETES;
                        pathList[e][2] = node3[e%c];
                    }
                }
            } else if (m.Evaluate(nd2, true).IsTrue() && m.Evaluate(nd4, true).IsTrue()) {
                LOGGER.info("nd2 and nd4");
                pathList = new String[1][3];
                pathList[0][0] = DIABETES;
                pathList[0][1] = ACARBOSE;
                pathList[0][2] = INSULIN;

            } else if (m.Evaluate(nd0, true).IsTrue()) {
                LOGGER.info("nd0");
                pathList = new String[a+b][2];
                for (int i=0; i<a; i++) {
                    pathList[i][0] = DIABETES;
                    pathList[i][1] = node1[i % (node1.length)];
                }
                pathList[a][0] = DIABETES;
                pathList[a][1] = ACARBOSE;
            }

            for (int i = 0; i < pathList.length; i++) {
                for (int j = 0; j < pathList[0].length; j++) {
                    LOGGER.info(pathList[i][j] + " ");
                }
                LOGGER.info("end of path");
            }

        }

        return pathList;
    }

    private String[][] findCOPDLevelUp(Context ctx, String[] prescribeDrugs) throws Z3Exception, TestFailedException {

        ArrayList<String> seletedMeds = new ArrayList<>();
        String[][] pathList = null;

        BoolExpr [] medi = new BoolExpr[4];
        medi[0] = ctx.MkBoolConst(COPD);
        medi[1] = ctx.MkBoolConst(CORTICOSTEROID);
        medi[2] = ctx.MkBoolConst(SHORTBRONCHODILATOR);
        medi[3] = ctx.MkBoolConst(LONGBRONCHODILATOR);

        BoolExpr nd0 = ctx.MkBoolConst("nd0");
        BoolExpr nd1 = ctx.MkBoolConst("nd1");
        BoolExpr nd2 = ctx.MkBoolConst("nd2");
        BoolExpr nd3 = ctx.MkBoolConst("nd3");

        ArrayList<String> list1 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(CORTICOSTEROID)));
        ArrayList<String> list2 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(SHORTBRONCHODILATOR)));
        ArrayList<String> list3 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(LONGBRONCHODILATOR)));

        String[] node1 = list1.toArray(new String[list1.size()]);
        String[] node2 = list2.toArray(new String[list2.size()]);
        String[] node3 = list3.toArray(new String[list3.size()]);

        LOGGER.info("node 1 path list ...");
        for(int i=0; i<node1.length; i++){
            LOGGER.info(node1[i]);
        }
        LOGGER.info("node 2 path list ...");
        for(int i=0; i<node2.length; i++){
            LOGGER.info(node2[i]);
        }
        LOGGER.info("node 3 path list ...");
        for(int i=0; i<node3.length; i++){
            LOGGER.info(node3[i]);
        }

        BoolExpr expNd0 = ctx.MkBoolConst("expNd0");
        BoolExpr expNd1 = ctx.MkBoolConst("expNd1");
        BoolExpr expNd2 = ctx.MkBoolConst("expNd2");
        BoolExpr expNd3 = ctx.MkBoolConst("expNd3");
        BoolExpr expNd4 = ctx.MkBoolConst("expNd4");
        BoolExpr expNd0R = ctx.MkBoolConst("expNd0R");
        BoolExpr expNd1R = ctx.MkBoolConst("expNd1R");
        BoolExpr expNd2R = ctx.MkBoolConst("expNd2R");
        BoolExpr expNd3R = ctx.MkBoolConst("expNd3R");
        BoolExpr expNd4R = ctx.MkBoolConst("expNd4R");
        BoolExpr exp1 = ctx.MkBoolConst("exp1");
        BoolExpr exp2 = ctx.MkBoolConst("exp2");
        BoolExpr exp3 = ctx.MkBoolConst("exp3");

        for(String med : prescribeDrugs){
            LOGGER.info("prescribed drug " + med);
            if(med.equalsIgnoreCase(COPD)){
                medi[0] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(CORTICOSTEROID)){
                medi[1] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(SHORTBRONCHODILATOR)){
                medi[2] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(LONGBRONCHODILATOR)){
                medi[3] = ctx.MkTrue();
            }
        }

        expNd0 = ctx.MkImplies(medi[0], nd0);
        expNd1 = ctx.MkImplies(medi[1], nd1);
        expNd2 = ctx.MkImplies(medi[2], nd2);
        expNd3 = ctx.MkImplies(medi[3], nd3);

        expNd0R = ctx.MkImplies(nd0, medi[0]);
        expNd1R = ctx.MkImplies(nd1, medi[1]);
        expNd2R = ctx.MkImplies(nd2, medi[2]);
        expNd3R = ctx.MkImplies(nd3, medi[3]);

        exp1 = ctx.MkImplies(nd0, ctx.MkAnd(new BoolExpr[]{nd1, nd2}));
        exp2 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd1, nd2}), nd0);
        exp3 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd1, nd2}), nd3);

        LOGGER.info("medi0 " + medi[0].IsTrue());
        LOGGER.info("medi1 " + medi[1].IsTrue());
        LOGGER.info("medi2 " + medi[2].IsTrue());
        LOGGER.info("medi3 " + medi[3].IsTrue());

        Solver s = ctx.MkSolver();

        s.Assert(expNd0);
        s.Assert(expNd1);
        s.Assert(expNd2);
        s.Assert(expNd3);

        if(medi[1].IsTrue() || medi[2].IsTrue()){
            s.Assert(exp2);
            s.Assert(exp3);
        }else if(medi[0].IsTrue()){
            s.Assert(exp1);
        }

        if (s.Check() == Status.SATISFIABLE) {
            Model m = s.Model();

            int a = node1.length;
            int b = node2.length;
            int c = node3.length;
            int e;

            LOGGER.info("nd0 " + m.Evaluate(nd0, true));
            LOGGER.info("nd1 " + m.Evaluate(nd1, true));
            LOGGER.info("nd2 " + m.Evaluate(nd2, true));
            LOGGER.info("nd3 " + m.Evaluate(nd3, true));

            if(m.Evaluate(nd3, true).IsTrue() && m.Evaluate(nd1, true).IsTrue()){
                LOGGER.info("nd3 & nd1 ......");
                pathList = new String[a*c][3];
                for(int i=0; i<a; i++){
                    for(int j=0; j<c; j++){
                        e = (i * c) + j;
                        if(e < c*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        pathList[e][0] = COPD;
                        pathList[e][2] = node3[e%c];
                    }

                }
            }else if(m.Evaluate(nd3, true).IsTrue() && m.Evaluate(nd2, true).IsTrue()){
                LOGGER.info("nd3 & nd2 ......");
                pathList = new String[b*c][3];
                for(int i=0; i<b; i++){
                    for(int j=0; j<c; j++){
                        e = (i * c) + j;
                        if(e < c*(i+1)){
                            pathList[e][1] = node2[i];
                        }
                        pathList[e][0] = COPD;
                        pathList[e][2] = node3[e%c];
                    }

                }
            }else if(m.Evaluate(nd0, true).IsTrue()){
                LOGGER.info("nd0 ......");
                pathList = new String[a+b][2];
                for(int i=0; i<a; i++){
                    pathList[i][0] = COPD;
                    pathList[i][1] = node1[i];
                }
                for(int j=a; j<a+b; j++){
                    pathList[j][0] = COPD;
                    pathList[j][1] = node2[j%b];
                }
            }

        }

        return pathList;

    }

    private String[][] findHypertensionLevelUp(Context ctx, String[] prescribeDrugs) throws Z3Exception, TestFailedException {


        ArrayList<String> seletedMeds = new ArrayList<>();
        String[][] pathList = null;

        BoolExpr [] medi = new BoolExpr[7];
        medi[0] = ctx.MkBoolConst(HYPERTENSION);
        medi[1] = ctx.MkBoolConst(ACE);
        medi[2] = ctx.MkBoolConst(ARB);
        medi[3] = ctx.MkBoolConst(CCB);
        medi[4] = ctx.MkBoolConst(DIURETIC);
        medi[5] = ctx.MkBoolConst(ALPHA);
        medi[6] = ctx.MkBoolConst(BETA);

        BoolExpr nd0 = ctx.MkBoolConst("nd0");
        BoolExpr nd1 = ctx.MkBoolConst("nd1");
        BoolExpr nd2 = ctx.MkBoolConst("nd2");
        BoolExpr nd3 = ctx.MkBoolConst("nd3");
        BoolExpr nd4 = ctx.MkBoolConst("nd4");
        BoolExpr nd5 = ctx.MkBoolConst("nd5");

        ArrayList<String> list1 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(ACE, ARB)));
        ArrayList<String> list2 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(CCB)));
        ArrayList<String> list3 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(DIURETIC)));
        ArrayList<String> list4 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(ALPHA, BETA)));

        String[] node1 = list1.toArray(new String[list1.size()]);
        String[] node2 = list2.toArray(new String[list2.size()]);
        String[] node3 = list3.toArray(new String[list3.size()]);
        String[] node4 = list4.toArray(new String[list4.size()]);

        BoolExpr expNd0 = ctx.MkBoolConst("expNd0");
        BoolExpr expNd1 = ctx.MkBoolConst("expNd1");
        BoolExpr expNd2 = ctx.MkBoolConst("expNd2");
        BoolExpr expNd3 = ctx.MkBoolConst("expNd3");
        BoolExpr expNd4 = ctx.MkBoolConst("expNd4");

        BoolExpr expNd0R = ctx.MkBoolConst("expNd0R");
        BoolExpr expNd1R = ctx.MkBoolConst("expNd1R");
        BoolExpr expNd2R = ctx.MkBoolConst("expNd2R");
        BoolExpr expNd3R = ctx.MkBoolConst("expNd3R");
        BoolExpr expNd4R = ctx.MkBoolConst("expNd4R");

        BoolExpr exp1 = ctx.MkBoolConst("exp1");
        BoolExpr exp2 = ctx.MkBoolConst("exp2");
        BoolExpr exp3 = ctx.MkBoolConst("exp3");
        BoolExpr exp4 = ctx.MkBoolConst("exp4");
        BoolExpr exp5 = ctx.MkBoolConst("exp5");
        BoolExpr exp6 = ctx.MkBoolConst("exp6");

        for(String med : prescribeDrugs){
            if(med.equalsIgnoreCase(HYPERTENSION)){
                medi[0] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(ACE)){
                medi[1] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(ARB)){
                medi[2] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(CCB)){
                medi[3] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(DIURETIC)){
                medi[4] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(ALPHA)){
                medi[5] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(BETA)){
                medi[6] = ctx.MkTrue();
            }
        }

        expNd0 = ctx.MkImplies(medi[0], nd0);
        expNd1 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[1], medi[2]}), nd1);
        expNd2 = ctx.MkImplies(medi[3], nd2);
        expNd3 = ctx.MkImplies(medi[4], nd3);
        expNd4 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[5], medi[6]}), nd4);

        expNd0R = ctx.MkImplies(nd0, medi[0]);
        expNd1R = ctx.MkImplies(nd1, ctx.MkAnd(new BoolExpr[]{medi[1], medi[2]}));
        expNd2R = ctx.MkImplies(nd2, medi[3]);
        expNd3R = ctx.MkImplies(nd3, medi[4]);
        expNd4R = ctx.MkImplies(nd4, ctx.MkAnd(new BoolExpr[]{medi[5], medi[6]}));

        exp1 = ctx.MkIff(nd0, nd1);
        exp2 = ctx.MkImplies(nd1, ctx.MkAnd(new BoolExpr[]{nd2, nd3}));
        exp3 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd2, nd3}), nd1);
        exp4 = ctx.MkImplies(nd2, ctx.MkAnd(new BoolExpr[]{nd3, nd4}));
        exp5 = ctx.MkImplies(nd3, ctx.MkAnd(new BoolExpr[]{nd1, nd2}));
        exp6 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd2, nd3}), nd4);

        Solver s = ctx.MkSolver();

        s.Assert(expNd0);
        s.Assert(expNd1);
        s.Assert(expNd2);
        s.Assert(expNd3);
        s.Assert(expNd4);
        LOGGER.info("before if conditions .................");
        if(medi[4].IsTrue()){
            s.Assert(exp6);
            s.Assert(exp5);
            s.Assert(exp1);
        }else if(medi[3].IsTrue()) {
            LOGGER.info("within CCB .................");
            s.Assert(exp6);
            s.Assert(exp4);
            s.Assert(exp3);
            s.Assert(exp1);
        }else if(medi[1].IsTrue() || medi[2].IsTrue()){
            s.Assert(exp1);
            s.Assert(exp2);
        }else if(medi[0].IsTrue()){
            LOGGER.info("within Hyp .................");
            s.Assert(exp1);
        }
        LOGGER.info("medi0 " + medi[0].IsTrue());
        LOGGER.info("medi1 " + medi[1].IsTrue());
        LOGGER.info("medi2 " + medi[2].IsTrue());
        LOGGER.info("medi3 " + medi[3].IsTrue());
        LOGGER.info("medi4 " + medi[4].IsTrue());
        LOGGER.info("medi5 " + medi[5].IsTrue());
        LOGGER.info("medi6 " + medi[6].IsTrue());

        if (s.Check() == Status.SATISFIABLE) {
            Model m = s.Model();

            int a = node1.length;
            int b = node2.length;
            int c = node3.length;
            int d = node4.length;
            int e;

            if(m.Evaluate(nd4, true).IsTrue() && m.Evaluate(nd3, true).IsTrue() && m.Evaluate(nd2, true).IsTrue()){
                pathList = new String[a*b*c*d + a*b*d + a*c*d][5];

                for(int i=0; i<a; i++){
                    for(int j=0; j<b; j++) {
                        for(int k=0; k<c; k++){
                            for(int l=0; l<d; l++) {
                                e = ((i * b * c * d) + (j * c * d) + (k * d + l));
                                if(e < (b*c*d)*(i+1)){
                                    pathList[e][1] = node1[i];
                                }
                                if(e < (c*d)*((i*b)+(j+1))){
                                    pathList[e][2] = node2[j];
                                }
                                if(e < d*((i*b*c)+(j*c)+(k+1))){
                                    pathList[e][3] = node3[k];
                                }
                                pathList[e][0] = HYPERTENSION;
                                pathList[e][4] = node4[e%d];
                            }
                        }
                    }
                }

                for(int i=0; i<a; i++) {
                    for(int j=0; j<b; j++){
                        for(int k=0; k<d; k++) {
                            e = ((a * b * c * d) + (i * b * d) + (j * d + k));
                            if((e - (a * b * c * d)) < (b*d)*((i*b)+(j+1))){
                                pathList[e][1] = node1[i];
                            }
                            if((e - (a * b * c * d)) < d*((i*b*d)+(j*d)+(k+1))){
                                pathList[e][2] = node2[j];
                            }
                            pathList[e][3] = node4[e%d];
                            if((e - ((a * b * c * d) + (a * b * d))) < (c*d)*((i*c)+(j+1))){
                                pathList[e][1] = node1[i];
                            }
                            if((e - ((a * b * c * d) + (a * b * d))) < d*((i*c*d)+(j*d)+(k+1))){
                                pathList[e][2] = node3[j];
                            }
                            pathList[e][0] = HYPERTENSION;
                            pathList[e][3] = node4[e%d];
                        }
                    }
                }

            }else if(m.Evaluate(nd1, true).IsTrue() && m.Evaluate(nd2, true).IsTrue() && m.Evaluate(nd3, true).IsTrue()){
                pathList = new String[a*(b+c)][3];
                for(int i=0; i<a; i++){
                    for(int j=0; j<(b+c); j++){
                        e = i * (b + c) + j;
                        if(e < (b+c)*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        if(e < (2*i+1)*b){
                            pathList[e][2] = node2[e%b];
                            continue;
                        }
                        if(e < (2*i+2)*c) {
                            pathList[e][2] = node3[e%c];
                        }
                        pathList[e][0] = HYPERTENSION;
                    }
                }

            }else if(m.Evaluate(nd0, true).IsTrue()){
                pathList = new String[a][2];
                for(int i=0; i<a; i++){
                    pathList[i][0] = HYPERTENSION;
                    pathList[i][1] = node1[i];
                }

            }

        }

        return pathList;
    }


    private String[][] findDiabetesLevelDown(Context ctx, String[] prescribeDrugs) throws Z3Exception, TestFailedException {


        String[][] pathList = null;

        BoolExpr[] medi = new BoolExpr[8];
        medi[0] = ctx.MkBoolConst(DIABETES);
        medi[1] = ctx.MkBoolConst(METFORMIN);
        medi[2] = ctx.MkBoolConst(SULFONYLUREA);
        medi[3] = ctx.MkBoolConst(ACARBOSE);
        medi[4] = ctx.MkBoolConst(SITAGLIPTIN);
        medi[5] = ctx.MkBoolConst(EMPAGLIFLOZIN);
        medi[6] = ctx.MkBoolConst(PIOGLITAZONE);
        medi[7] = ctx.MkBoolConst(INSULIN);


        BoolExpr nd0 = ctx.MkBoolConst("nd0");
        BoolExpr nd1 = ctx.MkBoolConst("nd1");
        BoolExpr nd2 = ctx.MkBoolConst("nd2");
        BoolExpr nd3 = ctx.MkBoolConst("nd3");
        BoolExpr nd4 = ctx.MkBoolConst("nd4");

        ArrayList<String> list1 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(METFORMIN, SULFONYLUREA)));
        ArrayList<String> list2 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(ACARBOSE)));
        ArrayList<String> list3 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(SITAGLIPTIN, PIOGLITAZONE, EMPAGLIFLOZIN)));

        String[] node1 = list1.toArray(new String[list1.size()]);
        String[] node2 = list2.toArray(new String[list2.size()]);
        String[] node3 = list3.toArray(new String[list3.size()]);

        BoolExpr expNd0 = ctx.MkBoolConst("expNd0");
        BoolExpr expNd1 = ctx.MkBoolConst("expNd1");
        BoolExpr expNd2 = ctx.MkBoolConst("expNd2");
        BoolExpr expNd3 = ctx.MkBoolConst("expNd3");
        BoolExpr expNd4 = ctx.MkBoolConst("expNd4");
        BoolExpr expNd0R = ctx.MkBoolConst("expNd0R");
        BoolExpr expNd1R = ctx.MkBoolConst("expNd1R");
        BoolExpr expNd2R = ctx.MkBoolConst("expNd2R");
        BoolExpr expNd3R = ctx.MkBoolConst("expNd3R");
        BoolExpr expNd4R = ctx.MkBoolConst("expNd4R");
        BoolExpr exp1 = ctx.MkBoolConst("exp1");
        BoolExpr exp2 = ctx.MkBoolConst("exp2");
        BoolExpr exp3 = ctx.MkBoolConst("exp3");
        BoolExpr exp4 = ctx.MkBoolConst("exp4");

        for (String drug : prescribeDrugs) {
            if (drug.equalsIgnoreCase(DIABETES)) {
                medi[0] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(METFORMIN)) {
                medi[1] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(SULFONYLUREA)) {
                medi[2] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(ACARBOSE)) {
                medi[3] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(SITAGLIPTIN)) {
                medi[4] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(EMPAGLIFLOZIN)) {
                medi[5] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(PIOGLITAZONE)) {
                medi[6] = ctx.MkTrue();
            } else if (drug.equalsIgnoreCase(INSULIN)) {
                medi[7] = ctx.MkTrue();
            }
        }

        expNd0 = ctx.MkImplies(medi[0], nd0);
        expNd1 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[1], medi[2]}), nd1);
        expNd2 = ctx.MkImplies(medi[3], nd2);
        expNd3 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[4], medi[5], medi[6]}), nd3);
        expNd4 = ctx.MkImplies(medi[7], nd4);

        expNd0R = ctx.MkImplies(nd0, medi[0]);
        expNd1R = ctx.MkImplies(nd1, ctx.MkAnd(new BoolExpr[]{medi[1], medi[2]}));
        expNd2R = ctx.MkImplies(nd2, medi[3]);
        expNd3R = ctx.MkImplies(nd3, ctx.MkAnd(new BoolExpr[]{medi[4], medi[5], medi[6]}));
        expNd4R = ctx.MkImplies(nd4, medi[7]);

        exp1 = ctx.MkImplies(nd4, nd3);
        exp2 = ctx.MkImplies(nd4, nd2);
        exp3 = ctx.MkImplies(nd3, nd1);
        exp4 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd1, nd2}), nd0);

        Solver s = ctx.MkSolver();

        s.Assert(expNd0);
        s.Assert(expNd1);
        s.Assert(expNd2);
        s.Assert(expNd3);
        s.Assert(expNd4);
        if (medi[7].IsTrue() && (medi[1].IsTrue() || medi[2].IsTrue() || medi[4].IsTrue() || medi[5].IsTrue() || medi[6].IsTrue())) {
            s.Assert(exp1);
            s.Assert(exp3);
            s.Assert(exp4);
            nd4 = ctx.MkFalse();
        } else if (medi[7].IsTrue() && medi[3].IsTrue()) {
            s.Assert(exp1);
            s.Assert(exp2);
            s.Assert(exp4);
            nd4 = ctx.MkFalse();
        } else if (medi[7].IsTrue()) {
            s.Assert(exp1);
            s.Assert(exp3);
            s.Assert(exp4);
            nd4 = ctx.MkFalse();
        } else if (medi[4].IsTrue() || medi[5].IsTrue() || medi[6].IsTrue()) {
            s.Assert(exp3);
            s.Assert(exp4);
            nd3 = ctx.MkFalse();
        } else if (medi[1].IsTrue() || medi[2].IsTrue()) {
            s.Assert(exp4);
            nd1 = ctx.MkFalse();
        } else if (medi[3].IsTrue()) {
            s.Assert(exp4);
            nd2 = ctx.MkFalse();
        }

        s.Assert(expNd0R);
        s.Assert(expNd1R);
        s.Assert(expNd2R);
        s.Assert(expNd3R);
        s.Assert(expNd4R);

        if (s.Check() == Status.SATISFIABLE) {
            Model m = s.Model();

            int a = node1.length;
            int b = node2.length;
            int c = node3.length;
            int e;

            if (m.Evaluate(nd3, true).IsTrue()) {
                pathList = new String[a*c][3];
                for(int i=0; i<a; i++){
                    for(int j=0; j<c; j++){
                        e = (i * c) + j;
                        if(e < c*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        pathList[e][0] = DIABETES;
                        pathList[e][2] = node3[e%c];
                    }
                }
            } else if (m.Evaluate(nd2, true).IsTrue() && m.Evaluate(nd4, true).IsTrue()) {
                pathList = new String[1][3];
                pathList[0][0] = DIABETES;
                pathList[0][1] = ACARBOSE;
                pathList[0][2] = INSULIN;

            } else if (m.Evaluate(nd0, true).IsTrue()) {
                pathList = new String[a+b][2];
                for (int i=0; i<a; i++) {
                    pathList[i][0] = DIABETES;
                    pathList[i][1] = node1[i];
                }
                pathList[a][0] = DIABETES;
                pathList[a][1] = ACARBOSE;
            }

        }

        return pathList;
    }


    private String[][] findCOPDLevelDown(Context ctx, String[] prescribeDrugs) throws Z3Exception, TestFailedException {

        String[][] pathList = null;

        BoolExpr [] medi = new BoolExpr[4];
        medi[0] = ctx.MkBoolConst(COPD);
        medi[1] = ctx.MkBoolConst(CORTICOSTEROID);
        medi[2] = ctx.MkBoolConst(SHORTBRONCHODILATOR);
        medi[3] = ctx.MkBoolConst(LONGBRONCHODILATOR);

        BoolExpr nd0 = ctx.MkBoolConst("nd0");
        BoolExpr nd1 = ctx.MkBoolConst("nd1");
        BoolExpr nd2 = ctx.MkBoolConst("nd2");
        BoolExpr nd3 = ctx.MkBoolConst("nd3");

        ArrayList<String> list1 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(CORTICOSTEROID)));
        ArrayList<String> list2 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(SHORTBRONCHODILATOR)));
        ArrayList<String> list3 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(LONGBRONCHODILATOR)));

        String[] node1 = list1.toArray(new String[list1.size()]);
        String[] node2 = list2.toArray(new String[list2.size()]);
        String[] node3 = list3.toArray(new String[list3.size()]);

        BoolExpr expNd0 = ctx.MkBoolConst("expNd0");
        BoolExpr expNd1 = ctx.MkBoolConst("expNd1");
        BoolExpr expNd2 = ctx.MkBoolConst("expNd2");
        BoolExpr expNd3 = ctx.MkBoolConst("expNd3");
        BoolExpr expNd4 = ctx.MkBoolConst("expNd4");
        BoolExpr expNd0R = ctx.MkBoolConst("expNd0R");
        BoolExpr expNd1R = ctx.MkBoolConst("expNd1R");
        BoolExpr expNd2R = ctx.MkBoolConst("expNd2R");
        BoolExpr expNd3R = ctx.MkBoolConst("expNd3R");
        BoolExpr expNd4R = ctx.MkBoolConst("expNd4R");
        BoolExpr exp1 = ctx.MkBoolConst("exp1");
        BoolExpr exp2 = ctx.MkBoolConst("exp2");
        BoolExpr exp3 = ctx.MkBoolConst("exp3");

        for(String med : prescribeDrugs){
            if(med.equalsIgnoreCase(COPD)){
                medi[0] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(CORTICOSTEROID)){
                medi[1] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(SHORTBRONCHODILATOR)){
                medi[2] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(LONGBRONCHODILATOR)){
                medi[3] = ctx.MkTrue();
            }
        }

        expNd0 = ctx.MkImplies(medi[0], nd0);
        expNd1 = ctx.MkImplies(medi[1], nd1);
        expNd2 = ctx.MkImplies(medi[2], nd2);
        expNd3 = ctx.MkImplies(medi[3], nd3);

        expNd0R = ctx.MkImplies(nd0, medi[0]);
        expNd1R = ctx.MkImplies(nd1, medi[1]);
        expNd2R = ctx.MkImplies(nd2, medi[2]);
        expNd3R = ctx.MkImplies(nd3, medi[3]);

        exp1 = ctx.MkImplies(nd3, nd1);
        exp2 = ctx.MkImplies(nd3, nd2);
        exp3 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd1, nd2}), nd0);

        Solver s = ctx.MkSolver();

        s.Assert(expNd0);
        s.Assert(expNd1);
        s.Assert(expNd2);
        s.Assert(expNd3);

        if(medi[3].IsTrue() && medi[1].IsTrue()){
            s.Assert(exp1);
            s.Assert(exp3);
            nd3 = ctx.MkFalse();
        }else if(medi[3].IsTrue() && medi[2].IsTrue()){
            s.Assert(exp2);
            s.Assert(exp3);
            nd3 = ctx.MkFalse();
        }else if(medi[3].IsTrue()){
            s.Assert(exp1);
            s.Assert(exp2);
            s.Assert(exp3);
            nd3 = ctx.MkFalse();
        }else if(medi[1].IsTrue()){
            s.Assert(exp3);
            nd1 = ctx.MkFalse();
        }else if(medi[2].IsTrue()){
            s.Assert(exp3);
            nd2 = ctx.MkFalse();
        }

        if (s.Check() == Status.SATISFIABLE) {
            Model m = s.Model();

            int a = node1.length;
            int b = node2.length;
            int c = node3.length;
            int e;

            if(m.Evaluate(nd1, true).IsTrue() && m.Evaluate(nd2, true).IsTrue()){
                pathList = new String[a+b][2];
                for(int i=0; i<a; i++){
                    pathList[i][0] = COPD;
                    pathList[i][1] = node1[i];
                }
                for(int j=a; j<a+b; j++){
                    pathList[j][0] = COPD;
                    pathList[j][1] = node2[j%b];
                }
            }else if(m.Evaluate(nd1, true).IsTrue()){
                pathList = new String[a][2];
                for(int i=0; i<a; i++){
                    pathList[i][1] = node1[i];
                    pathList[i][0] = COPD;
                }
            }else if(m.Evaluate(nd2, true).IsTrue()){
                pathList = new String[b][2];
                for(int i=0; i<b; i++){
                    pathList[i][1] = node1[i];
                    pathList[i][0] = COPD;
                }
            }

        }

        return pathList;
    }

    private String[][] findHypertensionLevelDown(Context ctx, String[] prescribeDrugs) throws Z3Exception, TestFailedException {


        ArrayList<String> seletedMeds = new ArrayList<>();
        String[][] pathList = null;

        BoolExpr [] medi = new BoolExpr[7];
        medi[0] = ctx.MkBoolConst(HYPERTENSION);
        medi[1] = ctx.MkBoolConst(ACE);
        medi[2] = ctx.MkBoolConst(ARB);
        medi[3] = ctx.MkBoolConst(CCB);
        medi[4] = ctx.MkBoolConst(DIURETIC);
        medi[5] = ctx.MkBoolConst(ALPHA);
        medi[6] = ctx.MkBoolConst(BETA);

        BoolExpr nd0 = ctx.MkBoolConst("nd0");
        BoolExpr nd1 = ctx.MkBoolConst("nd1");
        BoolExpr nd2 = ctx.MkBoolConst("nd2");
        BoolExpr nd3 = ctx.MkBoolConst("nd3");
        BoolExpr nd4 = ctx.MkBoolConst("nd4");
        BoolExpr nd5 = ctx.MkBoolConst("nd5");

        ArrayList<String> list1 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(ACE, ARB)));
        ArrayList<String> list2 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(CCB)));
        ArrayList<String> list3 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(DIURETIC)));
        ArrayList<String> list4 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(ALPHA, BETA)));

        String[] node1 = list1.toArray(new String[list1.size()]);
        String[] node2 = list2.toArray(new String[list2.size()]);
        String[] node3 = list3.toArray(new String[list3.size()]);
        String[] node4 = list4.toArray(new String[list4.size()]);

        BoolExpr expNd0 = ctx.MkBoolConst("expNd0");
        BoolExpr expNd1 = ctx.MkBoolConst("expNd1");
        BoolExpr expNd2 = ctx.MkBoolConst("expNd2");
        BoolExpr expNd3 = ctx.MkBoolConst("expNd3");
        BoolExpr expNd4 = ctx.MkBoolConst("expNd4");

        BoolExpr expNd0R = ctx.MkBoolConst("expNd0R");
        BoolExpr expNd1R = ctx.MkBoolConst("expNd1R");
        BoolExpr expNd2R = ctx.MkBoolConst("expNd2R");
        BoolExpr expNd3R = ctx.MkBoolConst("expNd3R");
        BoolExpr expNd4R = ctx.MkBoolConst("expNd4R");

        BoolExpr exp1 = ctx.MkBoolConst("exp1");
        BoolExpr exp2 = ctx.MkBoolConst("exp2");
        BoolExpr exp3 = ctx.MkBoolConst("exp3");
        BoolExpr exp4 = ctx.MkBoolConst("exp4");
        BoolExpr exp5 = ctx.MkBoolConst("exp5");
        BoolExpr exp6 = ctx.MkBoolConst("exp6");

        for(String med : prescribeDrugs){
            if(med.equalsIgnoreCase(HYPERTENSION)){
                medi[0] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(ACE)){
                medi[1] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(ARB)){
                medi[2] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(CCB)){
                medi[3] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(DIURETIC)){
                medi[4] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(ALPHA)){
                medi[5] = ctx.MkTrue();
            }else if(med.equalsIgnoreCase(BETA)){
                medi[6] = ctx.MkTrue();
            }
        }

        expNd0 = ctx.MkImplies(medi[0], nd0);
        expNd1 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[1], medi[2]}), nd1);
        expNd2 = ctx.MkImplies(medi[3], nd2);
        expNd3 = ctx.MkImplies(medi[4], nd3);
        expNd4 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{medi[5], medi[6]}), nd4);

        expNd0R = ctx.MkImplies(nd0, medi[0]);
        expNd1R = ctx.MkImplies(nd1, ctx.MkAnd(new BoolExpr[]{medi[1], medi[2]}));
        expNd2R = ctx.MkImplies(nd2, medi[3]);
        expNd3R = ctx.MkImplies(nd3, medi[4]);
        expNd4R = ctx.MkImplies(nd4, ctx.MkAnd(new BoolExpr[]{medi[5], medi[6]}));

        exp1 = ctx.MkImplies(nd4, nd3);
        exp2 = ctx.MkImplies(nd4, nd2);
        exp3 = ctx.MkImplies(nd3, nd2);
        exp4 = ctx.MkImplies(ctx.MkOr(new BoolExpr[]{nd3, nd2}), nd1);
        exp5 = ctx.MkImplies(nd1, nd0);

        Solver s = ctx.MkSolver();

        s.Assert(expNd0);
        s.Assert(expNd1);
        s.Assert(expNd2);
        s.Assert(expNd3);
        s.Assert(expNd4);

        if((medi[5].IsTrue() || medi[6].IsTrue()) && medi[4].IsTrue()){
            s.Assert(exp1);
            s.Assert(exp3);
            s.Assert(exp4);
            s.Assert(exp5);
            nd4 = ctx.MkFalse();
        }else if((medi[5].IsTrue() || medi[6].IsTrue()) && medi[2].IsTrue()){
            s.Assert(exp2);
            s.Assert(exp4);
            s.Assert(exp5);
            nd4 = ctx.MkFalse();
        }else if(medi[5].IsTrue() || medi[6].IsTrue()){
            s.Assert(exp1);
            s.Assert(exp2);
            s.Assert(exp3);
            s.Assert(exp4);
            s.Assert(exp5);
            nd4 = ctx.MkFalse();
        }else if(medi[3].IsTrue() && medi[4].IsTrue()) {
            s.Assert(exp3);
            s.Assert(exp4);
            s.Assert(exp5);
            nd3 = ctx.MkFalse();
        }else if(medi[4].IsTrue()){
            s.Assert(exp4);
            s.Assert(exp5);
            nd3 = ctx.MkFalse();
        }else if(medi[3].IsTrue()){
            s.Assert(exp4);
            s.Assert(exp5);
            nd2 = ctx.MkFalse();
        }

        if (s.Check() == Status.SATISFIABLE) {
            Model m = s.Model();

            int a = node1.length;
            int b = node2.length;
            int c = node3.length;
            int d = node4.length;
            int e;

            if(m.Evaluate(nd3, true).IsTrue() && m.Evaluate(nd2, true).IsTrue()){
                pathList = new String[a*b*c][4];

                for(int i=0; i<a; i++) {
                    for(int j=0; j<b; j++){
                        for(int k=0; k<c; k++) {
                            e = (i * b * c) + (j * c + k);
                            if(e < (b*c)*((i*b)+(j+1))){
                                pathList[e][1] = node1[i];
                            }
                            if(e < c*((i*b*c)+(j*c)+(k+1))){
                                pathList[e][2] = node2[j];
                            }
                            pathList[e][3] = node3[e%c];
                            pathList[e][0] = HYPERTENSION;
                        }
                    }
                }
            }else if(m.Evaluate(nd3, true).IsTrue()){
                pathList = new String[a*c][3];
                for(int i=0; i<a; i++){
                    for(int j=0; j<c; j++){
                        e = i * c + j;
                        if(e < c*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        pathList[e][2] = node3[e%c];
                        pathList[e][0] = HYPERTENSION;
                    }
                }
            }else if(m.Evaluate(nd2, true).IsTrue()){
                pathList = new String[a*b][3];
                for(int i=0; i<a; i++){
                    for(int j=0; j<b; j++){
                        e = i * b + j;
                        if(e < b*(i+1)){
                            pathList[e][1] = node1[i];
                        }
                        pathList[e][2] = node2[e%b];
                        pathList[e][0] = HYPERTENSION;
                    }
                }
            }
        }

        return pathList;
    }

    private String[][] buildArray(String[] node, int index, String[] prescribedDrugs, String changedDrug){
        String[][] pathList = null;
        pathList = new String[node.length][prescribedDrugs.length];
        for(int j=0; j<prescribedDrugs.length; j++){
            pathList[0][j] = prescribedDrugs[j];
        }
        for(int i=1; i<node.length; i++){
            if(!node[i].equalsIgnoreCase(changedDrug))
                pathList[i][index] = node[i];
            for(int j=0; j<prescribedDrugs.length; j++){
                if(j!=index)
                    pathList[i][j] = prescribedDrugs[j];
            }
        }
        return pathList;
    }


    public String[][] findSuggestionPathList(String disease, String[] prescribedDrugs, String changedDrug) throws Z3Exception {

        int index = Arrays.asList(prescribedDrugs).indexOf(changedDrug);

        if(disease.equalsIgnoreCase(DIABETES)){
            ArrayList<String> list1 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(METFORMIN, SULFONYLUREA)));
            ArrayList<String> list3 = drugController.getDrugListByCategoryList(1, new ArrayList<String>(Arrays.asList(SITAGLIPTIN, PIOGLITAZONE, EMPAGLIFLOZIN)));

            String[] node1 = list1.toArray(new String[list1.size()]);
            String[] node3 = list3.toArray(new String[list3.size()]);
            if(list1.contains(changedDrug)){
                return buildArray(node1, index, prescribedDrugs, changedDrug);
            }else if(list3.contains(changedDrug)){
                return buildArray(node3, index, prescribedDrugs, changedDrug);
            }
        }else if(disease.equalsIgnoreCase(COPD)){

            ArrayList<String> list1 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(CORTICOSTEROID)));
            ArrayList<String> list2 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(SHORTBRONCHODILATOR)));
            ArrayList<String> list3 = drugController.getDrugListByCategoryList(3, new ArrayList<String>(Arrays.asList(LONGBRONCHODILATOR)));

            String[] node1 = list1.toArray(new String[list1.size()]);
            String[] node2 = list2.toArray(new String[list2.size()]);
            String[] node3 = list3.toArray(new String[list3.size()]);

            if(list1.contains(changedDrug)){
                return buildArray(node1, index, prescribedDrugs, changedDrug);
            }else if(list2.contains(changedDrug)){
                return buildArray(node2, index, prescribedDrugs, changedDrug);
            }else if(list3.contains(changedDrug)){
                return buildArray(node3, index, prescribedDrugs, changedDrug);
            }

        }else if(disease.equalsIgnoreCase(HYPERTENSION)){
            ArrayList<String> list1 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(ACE, ARB)));
            ArrayList<String> list2 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(CCB)));
            ArrayList<String> list3 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(DIURETIC)));
            ArrayList<String> list4 = drugController.getDrugListByCategoryList(2, new ArrayList<String>(Arrays.asList(ALPHA, BETA)));

            String[] node1 = list1.toArray(new String[list1.size()]);
            String[] node2 = list2.toArray(new String[list2.size()]);
            String[] node3 = list3.toArray(new String[list3.size()]);
            String[] node4 = list4.toArray(new String[list4.size()]);

            if(list1.contains(changedDrug)){
                return buildArray(node1, index, prescribedDrugs, changedDrug);
            }else if(list2.contains(changedDrug)){
                return buildArray(node2, index, prescribedDrugs, changedDrug);
            }else if(list3.contains(changedDrug)){
                return buildArray(node3, index, prescribedDrugs, changedDrug);
            }else if(list4.contains(changedDrug)){
                return buildArray(node4, index, prescribedDrugs, changedDrug);
            }
        }

        return null;

    }

    public String[][] findDiseaseLevelUpPathList(String disease, String[] prescribeDrugs) throws Z3Exception, TestFailedException {

        Context.ToggleWarningMessages(true);
        HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);

        if(disease.equalsIgnoreCase(DIABETES)){
            return findDiabetesLevelUp(ctx, prescribeDrugs);
        }else if(disease.equalsIgnoreCase(COPD)){
            return findCOPDLevelUp(ctx, prescribeDrugs);
        }else if(disease.equalsIgnoreCase(HYPERTENSION)){
            return findHypertensionLevelUp(ctx, prescribeDrugs);
        }

        return null;
    }

    public String[][] findDiseaseLevelDownPathList(String disease, String[] prescribeDrugs) throws Z3Exception, TestFailedException {

        Context.ToggleWarningMessages(true);
        HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);

        if(disease.equalsIgnoreCase(DIABETES)){
            return findDiabetesLevelDown(ctx, prescribeDrugs);
        }else if(disease.equalsIgnoreCase(COPD)){
            return findCOPDLevelDown(ctx, prescribeDrugs);
        }else if(disease.equalsIgnoreCase(HYPERTENSION)){
            return findHypertensionLevelDown(ctx, prescribeDrugs);
        }

        return null;
    }

}
