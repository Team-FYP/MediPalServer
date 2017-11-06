package lk.ac.mrt.cse.medipal.model;

public class Drug {

    private String drug_id;
    private String drug_name;
    private String category_id;
    private String disease_id;


    public Drug(String drug_id, String drug_name, String category_id, String disease_id) {
        this.drug_id = drug_id;
        this.drug_name = drug_name;
        this.category_id = category_id;
        this.disease_id = disease_id;
    }

    public Drug(){
    }

    public String getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
