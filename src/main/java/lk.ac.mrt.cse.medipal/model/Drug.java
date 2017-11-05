package lk.ac.mrt.cse.medipal.model;

public class Drug {

    private String drug_id;
    private String drug_name;
    private int category_id;


    public Drug(String drug_id, String drug_name, int category_id) {
        this.drug_id = drug_id;
        this.drug_name = drug_name;
        this.category_id = category_id;
    }

    public Drug(){
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
