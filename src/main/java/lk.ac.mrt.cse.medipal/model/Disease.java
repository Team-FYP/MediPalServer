package lk.ac.mrt.cse.medipal.model;

public class Disease {
    private int disease_id;
    private String disease_name;
    private int graph_graph_id;


    public Disease(int disease_id, String disease_name, int graph_graph_id) {
        this.disease_id = disease_id;
        this.disease_name = disease_name;
        this.graph_graph_id = graph_graph_id;
    }

    public Disease(){
    }

    public int getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(int disease_id) {
        this.disease_id = disease_id;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public int getGraph_graph_id() {
        return graph_graph_id;
    }

    public void setGraph_graph_id(int graph_graph_id) {
        this.graph_graph_id = graph_graph_id;
    }
}
