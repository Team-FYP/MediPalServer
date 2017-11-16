package lk.ac.mrt.cse.medipal.model;

public class DrugCategory {
    private String category_id;
    private String category_name;
    private String graph_id;

    public DrugCategory(String category_id, String category_name, String graph_id) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.graph_id = graph_id;
    }

    public DrugCategory(){}

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getGraph_id() {
        return graph_id;
    }

    public void setGraph_id(String graph_id) {
        this.graph_id = graph_id;
    }
}
