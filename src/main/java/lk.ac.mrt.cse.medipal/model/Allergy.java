package lk.ac.mrt.cse.medipal.model;

public class Allergy {
    private int allergy_id;
    private String allergy_name;

    public Allergy(int allergy_id, String allergy_name) {
        this.allergy_id = allergy_id;
        this.allergy_name = allergy_name;
    }

    public Allergy(){

    }

    public int getAllergy_id() {
        return allergy_id;
    }

    public void setAllergy_id(int allergy_id) {
        this.allergy_id = allergy_id;
    }

    public String getAllergy_name() {
        return allergy_name;
    }

    public void setAllergy_name(String allergy_name) {
        this.allergy_name = allergy_name;
    }
}
