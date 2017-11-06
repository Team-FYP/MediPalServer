package lk.ac.mrt.cse.medipal.model;

public class Disease {
    private String disease_id;
    private String disease_name;
    private String graph_graph_id;


    public Disease(String disease_id, String disease_name, String graph_graph_id) {
        this.disease_id = disease_id;
        this.disease_name = disease_name;
        this.graph_graph_id = graph_graph_id;
    }

    public Disease(){
    }

    public String getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public String getGraph_graph_id() {
        return graph_graph_id;
    }

    public void setGraph_graph_id(String graph_graph_id) {
        this.graph_graph_id = graph_graph_id;
    }
}
